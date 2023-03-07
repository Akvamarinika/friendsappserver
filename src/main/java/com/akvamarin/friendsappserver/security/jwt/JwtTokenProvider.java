package com.akvamarin.friendsappserver.security.jwt;

import com.akvamarin.friendsappserver.domain.entity.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Date;

/**
 * Класс генерирует, валидирует JWT token
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public String createToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now(); //Unix-time (сек, наносек)
        //Key secret = Keys.hmacShaKeyFor(Decoders.BASE64.encode(jwtProperties.getSecretKey()));

        return Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .setSubject(String.format("%s,%s", user.getId(), user.getUsername()))
                .setIssuedAt(Date.from(now)) //Токен выпущен (текущая дата и время)
                .setExpiration(Date.from(now.plusSeconds(jwtProperties.getTokenExpireDurationSec()))) // Срок действия токена
                .signWith(jwtProperties.getSecretKeyDecode(), SignatureAlgorithm.HS256) //Подписывается секретным ключом
                //.signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    //на основании токена, получить username
    public String getUsername(String token) {
        //Key secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfigProperties.getSecretKey()));
        String string = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecretKeyDecode())
                //.setSigningKey(jwtProperties.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        log.info("Method *** getUsername *** : claims.getSubject() = {}", string);
        return string.split(",")[1];
    }

    //на основании токена, получить ID user
    public String getUserID(String token) {
        //Key secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfigProperties.getSecretKey()));
        String string = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecretKeyDecode())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        log.info("Method *** getUserID *** : claims.getSubject() = {}", string);
        return string.split(",")[0];
    }

    //валидация токена полученного от user
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getSecretKeyDecode())
                    //.setSigningKey(jwtProperties.getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("JWT token expired", ex);
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty", ex);
        } catch (MalformedJwtException ex) {
            log.error("JWT token is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("JWT token is not supported", ex);
        } catch (SignatureException ex) {
            log.error("Signature validation failed");
        }

        return false;
    }

}
