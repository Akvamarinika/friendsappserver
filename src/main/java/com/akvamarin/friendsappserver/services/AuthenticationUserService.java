package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.AuthServerToken;
import com.akvamarin.friendsappserver.domain.dto.AuthUserParamDTO;
import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import lombok.NonNull;

public interface AuthenticationUserService {
    User register (UserDTO userDTO);
    AuthServerToken authentication(@NonNull AuthUserParamDTO authUserParamDTO);
}