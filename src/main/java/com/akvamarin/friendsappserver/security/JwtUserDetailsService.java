package com.akvamarin.friendsappserver.security;

import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/***
 * Класс используется Spring Security для того,
 * чтоб получить данные пользователя и проверить его права
 * ***/
@Slf4j
@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public User loadUserByUsername(String login) throws UsernameNotFoundException { // генерирует user по login(username)
        if (login.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")){
            User user =  userRepository
                    .findByEmail(login)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("Email: %s, not found", login)));
            log.info("Method *** loadUserByUsername *** : JWT User with username(email) = {}", login);
            return user;
       /* } else if (login.matches("^\\d{11}$")){
            return userRepository
                    .findByPhone(login)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("Phone: %s, not found", login))); */
        } else {
            User user = userRepository
                    .findByVkId(login)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("VK ID: %s, not found", login)));
                    log.info("Method *** loadUserByUsername *** : JWT User with username(VK ID) = {}", login);
           return user;
        }
    }
}
