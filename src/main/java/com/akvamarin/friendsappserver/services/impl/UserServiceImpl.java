package com.akvamarin.friendsappserver.services.impl;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.mapper.UserMapper;
import com.akvamarin.friendsappserver.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
//BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());

@Slf4j
@Service
@RequiredArgsConstructor    //конструктор с 1 параметром для каждого поля
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    //Новый пользователь:
    //если сущест-ет email / vkId
    @Transactional
    @Override
    public User createNewUser(@NotNull UserDTO userDTO) throws ValidationException {
        boolean isUserFromDBDuplicateEmail = userRepository.findByEmail(userDTO.getEmail()).isPresent();

        if (isUserFromDBDuplicateEmail){    //NonUniqueResultException
            throw new ValidationException("Email already registered!");
        }

        User user = userMapper.toEntity(userDTO);
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(hashedPassword);
        user.setEnabled(true);

        return userRepository.save(user); //return User from DB
    }

    @Transactional
    @Override
    public List<UserDTO> findAll() {
        List<UserDTO> result = userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Method *** findAll *** : UserDTO list size = {}", result.size());
        return result;
    }

    @Transactional
    @Override
    public UserDTO findById(long userID) {
        UserDTO result =  userRepository.findById(userID)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with ID %d not found", userID)));
        log.info("Method *** findById *** : UserDTO = {} UserID = {}", result, userID);
        return result;
    }

    @Override
    @Transactional
    public User updateUser(@NonNull UserDTO userDTO) {
        return userRepository.findById(userDTO.getId())
                .map(user -> {
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
                }).orElseThrow(EntityNotFoundException::new);
        log.info("Method *** deleteById *** : isDeletedUser = {} with ID = {}", isDeletedUser, id);
        return isDeletedUser;
    }

}
