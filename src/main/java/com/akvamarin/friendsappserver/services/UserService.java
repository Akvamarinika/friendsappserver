package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    //public User getUser(User user);
    User createNewUser(UserDTO userDTO);
    UserDTO findById(long userID);
}
