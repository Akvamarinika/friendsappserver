package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.AuthUserSocialDTO;
import com.akvamarin.friendsappserver.domain.dto.request.UserDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewUserDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewUserSlimDTO;
import com.akvamarin.friendsappserver.domain.entity.User;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService {
    //ВК регист-ия
    User createNewUserVKontakte(@NotNull AuthUserSocialDTO userSocialDTO) throws ValidationException;

    //public User getUser(User user);
    User createNewUser(@NotNull UserDTO userDTO);
    User createNewUserVK(@NotNull AuthUserSocialDTO userSocialDTO);
    List<ViewUserSlimDTO> findAll();
    ViewUserDTO findById(long userID);

    ViewUserDTO findByLogin(String login);

    User updateUser(UserDTO userDTO);
    boolean deleteById(long id);

    boolean isUsernameAlreadyTaken(String username);
}
