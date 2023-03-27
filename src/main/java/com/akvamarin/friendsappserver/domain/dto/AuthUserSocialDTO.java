package com.akvamarin.friendsappserver.domain.dto;

import com.akvamarin.friendsappserver.domain.enums.Sex;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Jacksonized
@Builder
@Data
public class AuthUserSocialDTO {

    @Size(min = 3, max = 64)
    @Email(message = "Email should be valid")
    @NotNull
    private String username;

    @Size(min = 3, max = 64)
    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 3, max = 32)
    @NotNull
    private String vkId;

    @NotNull
    private String socialToken;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String photo;

    @NotNull
    private String dateOfBirth;

    @NotNull
    private String city;

    @NotNull
    private String country;

    @NotNull
    private Sex sex;

    @NotNull
    private Set<String> roles;
}
