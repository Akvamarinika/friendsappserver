package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    //public User getUser(User user);
    User createNewUser(UserDTO userDTO);
    List<UserDTO> findAll();
    UserDTO findById(long userID);
    User updateUser(UserDTO userDTO);

}
