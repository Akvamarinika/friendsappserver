package com.akvamarin.friendsappserver.services;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.exception.NoSuchElementFoundException;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor    //конструктор с 1 параметром для каждого поля
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;

   // @Autowired
   // public UserServiceImpl(UserRepository userRepository) {
   //     this.userRepository = userRepository;
   // }

/*
    @Override
    public User getUser(User user) {
        TypedQuery<User> typedQuery = entityManager.createQuery(
                "FROM User WHERE password = :password AND username = :username", User.class);
        try {
            User userFromDB = typedQuery
                    .setParameter("password", user.getPassword())
                    .setParameter("username", user.getNickname())
                    .getSingleResult();
            return userFromDB;
        } catch (Exception e) {
            return null;
        }
    }
    */


    //Новый пользователь:
    //если сущест-ет email / vkId
    @Transactional
    @Override
    public User createNewUser(@NotNull UserDTO userDTO) {
        boolean isUserFromDBDuplicateEmail = userRepository.findUserByEmail(userDTO.getEmail()).isPresent();
        boolean isUserFromDBDuplicatePhone = userRepository.findUserByPhone(userDTO.getPhone()).isPresent();

        if (isUserFromDBDuplicateEmail){
            throw new ValidationException("Email already registered!");
        }

        if (isUserFromDBDuplicatePhone){
            throw new ValidationException("Phone number already registered!");
        }

        User user = userMapper.toEntity(userDTO);
        String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);



        return userRepository.save(user); //return User from DB
    }

    @Override
    public UserDTO findById(long userID) {
        return userRepository.findById(userID)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new NoSuchElementFoundException(String.format("User with ID %d not found", userID)));
    }
}
