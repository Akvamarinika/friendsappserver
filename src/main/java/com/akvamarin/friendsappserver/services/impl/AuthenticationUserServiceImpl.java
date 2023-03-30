package com.akvamarin.friendsappserver.services.impl;

import com.akvamarin.friendsappserver.domain.dto.*;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.mapper.UserMapper;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.security.JwtUserDetailsService;
import com.akvamarin.friendsappserver.security.jwt.JwtTokenProvider;
import com.akvamarin.friendsappserver.security.social.VkProperties;
import com.akvamarin.friendsappserver.services.AuthenticationUserService;
import com.akvamarin.friendsappserver.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    //VK
    @Override
    public AuthServerToken authOAuth2(@NonNull AuthUserSocialDTO userSocialDTO) {
        CheckTokenVkResponse checkTokenVkResponse;
        try {
            checkTokenVkResponse = requestTokenUserVK(userSocialDTO);
        } catch (JsonProcessingException ex) {
            log.error("Method *** authOAuth2 *** : Error = {}", ex.getMessage());
            throw new ValidationException("Token validation failed with an error.");
        }

        log.info("Method *** authOAuth2 *** : checkTokenVkResponse = {} ", checkTokenVkResponse);
        if (checkTokenVkResponse.getSuccess() == 1) {
            userService.createNewUserVK(userSocialDTO);

            UserDetails userDetails = userDetailsService.loadUserByUsername(userSocialDTO.getUsername());
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenProvider.createToken(authentication);
            User user = (User) authentication.getPrincipal();
            log.info("Method *** authOAuth2 *** : Username = {} logged with Vkontakte", user.getUsername());
            return new AuthServerToken (token);
        }

        throw new ValidationException("Token VK is invalid.");
    }

    //проверка ВК токена
    private CheckTokenVkResponse requestTokenUserVK(AuthUserSocialDTO userSocialDTO) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder.fromUriString(vkProperties.getUrlSecure())
                .queryParam("v", vkProperties.getVersion())
                .queryParam("token", userSocialDTO.getSocialToken())
                .queryParam("client_secret", vkProperties.getSecretKey())
                .queryParam("access_token", vkProperties.getAccessToken())
                .build()
                .toUri();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        String response = responseEntity.getBody();
        int statusCode = responseEntity.getStatusCodeValue();

        log.info("Method *** authOAuth2 *** : uri = {} response = {} status code = {}", uri, response, statusCode);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);


        if (statusCode == 200 && jsonNode.has("response")) {
            int success = jsonNode.get("response").get("success").asInt();
            long date = jsonNode.get("response").get("date").asLong();
            int expire = jsonNode.get("response").get("expire").asInt();
            long userId = jsonNode.get("response").get("user_id").asLong();

            return new CheckTokenVkResponse(success, date, expire, userId);
        }

        throw new ValidationException("Your VK token is not valid!");
    }

}


