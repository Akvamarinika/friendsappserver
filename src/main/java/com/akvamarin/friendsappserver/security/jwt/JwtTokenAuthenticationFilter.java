package com.akvamarin.friendsappserver.security.jwt;

import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.security.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс фильтрует запросы, на наличие JWT token.
 *
 * Гарантирует однократное выполнение каждого запроса.
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUserDetailsService userDetailsService;
    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJwtTokenFromRequest(request);

            if (StringUtils.hasText(token) && tokenProvider.validateAccessToken(token)) { //когда токен прошел валидацию

                UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        filterChain.doFilter(request, response);

    }

    //получить токен из запроса
    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);  //"Authorization"

        if (bearerToken != null && bearerToken.startsWith(JwtProperties.BEARER)) { //"Bearer "
            return bearerToken.substring(JwtProperties.BEARER.length());
        }

        return null;
    }

    //на основании токена, возвращает данные об аутентификации
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String username = tokenProvider.getUsername(token);
        User user = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()); //null
    }

}
