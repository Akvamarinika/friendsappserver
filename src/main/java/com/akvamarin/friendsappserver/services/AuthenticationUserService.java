package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.AuthServerToken;
import com.akvamarin.friendsappserver.domain.dto.AuthUserParamDTO;
import com.akvamarin.friendsappserver.domain.dto.AuthUserSocialDTO;
import com.akvamarin.friendsappserver.domain.dto.request.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import lombok.NonNull;

public interface AuthenticationUserService {
    User registration(@NonNull UserDTO userDTO);
    AuthServerToken authentication(@NonNull AuthUserParamDTO authUserParamDTO);
    AuthServerToken authOAuth2(@NonNull AuthUserSocialDTO userSocialDTO);
}
