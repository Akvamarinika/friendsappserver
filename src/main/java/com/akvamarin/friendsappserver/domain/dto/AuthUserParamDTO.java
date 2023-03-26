package com.akvamarin.friendsappserver.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Jacksonized
@Builder
@Data
public class AuthUserParamDTO {

    @Size(min = 3, max = 64)
    @Email(message = "Email should be valid")
    @NotNull
    private String username;

    @Size(min = 3, max = 40)
    @NotNull
    private String password;
}
