package com.akvamarin.friendsappserver.services.impl;

import com.akvamarin.friendsappserver.domain.dto.AuthServerToken;
import com.akvamarin.friendsappserver.domain.dto.AuthUserParamDTO;
import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.security.jwt.JwtTokenProvider;
import com.akvamarin.friendsappserver.services.AuthenticationUserService;
import com.akvamarin.friendsappserver.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor    //конструктор с 1 параметром для каждого поля
public class AuthenticationUserServiceImpl implements AuthenticationUserService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    public User register(@NonNull UserDTO userDTO) {
        User registeredUser = userService.createNewUser(userDTO);
        log.info("Method *** register *** : User = {}", registeredUser);
        return registeredUser;
    }

    @Override
    public AuthServerToken authentication(@NonNull AuthUserParamDTO authUserParamDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authUserParamDTO.getUsername(), authUserParamDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);

        User user = (User) authentication.getPrincipal();
        log.info("Method *** authentication *** : Username = {} logged!", user.getUsername());

        return new AuthServerToken (token);
    }

}


