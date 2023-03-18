package com.akvamarin.friendsappserver.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

@Jacksonized
@Builder
@Data
public class AuthUserParamDTO {

    @NotNull
    private String username;

    private String password;
}
