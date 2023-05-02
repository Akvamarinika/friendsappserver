package com.akvamarin.friendsappserver.services.impl;
import com.akvamarin.friendsappserver.domain.dto.AuthUserSocialDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewUserDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewUserSlimDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.domain.dto.request.UserDTO;
import com.akvamarin.friendsappserver.domain.mapper.UserMapper;
import com.akvamarin.friendsappserver.repositories.location.CityRepository;
import com.akvamarin.friendsappserver.services.CityService;
import com.akvamarin.friendsappserver.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CityService cityService;
    private final CityRepository cityRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    //ВК сохраненение данных польз-ля, если их нет в БД
    @Transactional
    @Override
    public User createNewUserVKontakte(@NotNull AuthUserSocialDTO userSocialDTO) {
        Optional<User> userOptional = userRepository.findByVkId(userSocialDTO.getVkId());
        log.info("Method *** createNewUserVKontakte *** : user VK isPresent = {} ", userOptional.isPresent());

        if (userOptional.isPresent()) {
            return userOptional.get();
        }

        City city = cityService.findCityIfNoCreateNew(userSocialDTO.getCity(), userSocialDTO.getCountry());
        User user = userMapper.toEntity(userSocialDTO);
        user.setCity(city);
        user.setEnabled(true);

        log.info("Method *** createNewUserVKontakte *** : Username = {} vkID = {}", user.getUsername(), user.getVkId());
        log.info("Method *** createNewUserVKontakte *** : User = {} ", user);
        return userRepository.save(user);
    }

    //обычная регист-ия
    @Transactional
    @Override
    public User createNewUser(@NotNull UserDTO userDTO) throws ValidationException {
        boolean isUserFromDBDuplicateEmail = userRepository.findByEmail(userDTO.getEmail()).isPresent();

        if (isUserFromDBDuplicateEmail) {
           throw new ValidationException("Email already registered!");
        }

        User user = userMapper.toEntity(userDTO);

        City city = null;
        if (userDTO.getCityID() != null) {
            city = cityRepository.findById(userDTO.getCityID())
                    .orElseThrow(EntityNotFoundException::new);
        }

        if (userDTO.getPassword() != null){
            String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(hashedPassword);
        }

        user.setCity(city);
        user.setEnabled(true);

        return userRepository.save(user); //return User from DB
    }

    @Transactional
    @Override
    public List<ViewUserSlimDTO> findAll() {
        List<ViewUserSlimDTO> result = userRepository.findAll().stream()
                .map(userMapper::userToViewUserSlimDTO)
                .collect(Collectors.toList());
        log.info("Method *** findAll *** : ViewUserSlimDTO list size = {}", result.size());
        return result;
    }

    @Transactional
    @Override
    public ViewUserDTO findById(long userID) {
        ViewUserDTO result = userRepository.findById(userID)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with ID %d not found", userID)));
        log.info("Method *** findById *** : UserDTO = {} UserID = {}", result, userID);
        return result;
    }

    @Override
    @Transactional
    public ViewUserDTO findByLogin(String login) {
        ViewUserDTO result = userRepository.findUserByUsername(login)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with login %s not found", login)));
        log.info("Method *** findById *** : UserDTO = {} Login = {}", result, login);
        return result;
    }

    @Override
    @Transactional
    public User updateUser(@NonNull UserDTO userDTO) {
        return userRepository.findById(userDTO.getId())
                .map(user -> {
                    if (userDTO.getPassword() != null){
                        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
                        userDTO.setPassword(hashedPassword);
                    }

                    City city = cityRepository.findById(userDTO.getCityID())
                            .orElseThrow(EntityNotFoundException::new);

                    user.setCity(city);
                    userMapper.updateEntity(userDTO, user);
                    return userRepository.save(user);   // обновляет, если такой User есть
                })
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with ID %d not found", userDTO.getId())));
    }

    @Override
    @Transactional
    public boolean deleteById(long id) {
        boolean isDeletedUser = userRepository.findById(id)
                .map(user -> {
                    userRepository.deleteById(user.getId());
                    return true;
                }).orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
        log.info("Method *** deleteById *** : isDeletedUser = {} with ID = {}", isDeletedUser, id);
        return isDeletedUser;
    }

    @Transactional
    @Override
    public User createNewUserVK(@NotNull AuthUserSocialDTO userSocialDTO) throws ValidationException {
        boolean isUserFromDBDuplicateEmailOrVkId = userRepository.findByEmailOrVkId(
                userSocialDTO.getEmail(), userSocialDTO.getVkId()).isPresent();

        //TODO: определить точно: дубликат почты или ВК uuid

        if (isUserFromDBDuplicateEmailOrVkId) {
            throw new ValidationException("VK ID or Email already registered!");
        }

        City city = cityService.findCityIfNoCreateNew(userSocialDTO.getCity(), userSocialDTO.getCountry());
        User user = userMapper.toEntity(userSocialDTO);
        user.setCity(city);
        user.setEnabled(true);

        return userRepository.save(user); //return User from DB
    }

    @Transactional(readOnly = true)
    public boolean isUsernameAlreadyTaken(String username) {
        Optional<User> existingUser = userRepository.findUserByUsername(username);
        log.info("Method *** isUsernameAlreadyTaken *** : username = {} existingUser = {}", username, existingUser.isPresent());
        return existingUser.isPresent();
    }


}
