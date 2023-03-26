package com.akvamarin.friendsappserver.services.impl;

import com.akvamarin.friendsappserver.domain.dto.*;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.mapper.UserMapper;
import com.akvamarin.friendsappserver.repositories.CountryRepository;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.security.JwtUserDetailsService;
import com.akvamarin.friendsappserver.security.jwt.JwtTokenProvider;
import com.akvamarin.friendsappserver.security.social.VkProperties;
import com.akvamarin.friendsappserver.services.AuthenticationUserService;
import com.akvamarin.friendsappserver.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.ValidationException;
import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor    //конструктор с 1 параметром для каждого поля
public class AuthenticationUserServiceImpl implements AuthenticationUserService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final JwtUserDetailsService userDetailsService;
    private final VkProperties vkProperties;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;

    @Override
    public User registration(@NonNull UserDTO userDTO) {
        User registeredUser = userService.createNewUser(userDTO);
        log.info("Method *** register *** : User = {}", registeredUser);
        return registeredUser;
    }

    @Override
    public AuthServerToken authentication(@NonNull AuthUserParamDTO authUserParamDTO) {
        String dtoUsername = authUserParamDTO.getUsername();

        Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(dtoUsername, authUserParamDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);

        User user = (User) authentication.getPrincipal();
        log.info("Method *** authentication *** : Username = {} logged!", user.getUsername());

        return new AuthServerToken (token);
    }

    @Override
    public User registerOAuth2(@NonNull AuthUserSocialDTO userSocialDTO) {
        CheckTokenVkResponse checkTokenVkResponse;
        try {
            checkTokenVkResponse = requestTokenUserVK(userSocialDTO);
        } catch (JsonProcessingException ex) {
            log.error("Method *** authOAuth2 *** : Error = {}", ex.getMessage());
            throw new ValidationException("Token validation failed with an error.");
        }

        if (checkTokenVkResponse.getSuccess() == 1) {
            User user = userMapper.toEntity(userSocialDTO);
            user.setEnabled(true);
            log.info("Method *** registerOAuth2 *** : User = {}", user);
            return userRepository.save(user);
        }
        throw new ValidationException("Token is invalid.");
    }


    @Override
    public AuthServerToken authOAuth2(@NonNull AuthUserSocialDTO userSocialDTO) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userSocialDTO.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);
        User user = (User) authentication.getPrincipal();
        log.info("Method *** authVK *** : Username = {} logged with Vkontakte", user.getUsername());
        return new AuthServerToken (token);
    }

    private CheckTokenVkResponse requestTokenUserVK(AuthUserSocialDTO userSocialDTO) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder.fromUriString(vkProperties.getUrlSecure())
                .queryParam("token", userSocialDTO.getSocialToken())
                .queryParam("client_secret", vkProperties.getSecretKey())
                .queryParam("access_token", vkProperties.getAccessToken())
                .build()
                .toUri();

        String response = restTemplate.getForObject(uri, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, CheckTokenVkResponse.class);
    }

}


