package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.AuthUserSocialDTO;
import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService {
    //public User getUser(User user);
    User createNewUser(@NotNull UserDTO userDTO);
    User createNewUserVK(@NotNull AuthUserSocialDTO userSocialDTO);
    List<UserDTO> findAll();
    UserDTO findById(long userID);
    User updateUser(UserDTO userDTO);
    boolean deleteById(long id);


}
