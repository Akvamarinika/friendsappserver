package com.akvamarin.friendsappserver.domain.dto;

import com.akvamarin.friendsappserver.domain.enums.Sex;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Jacksonized
@Builder
@Data
public class AuthUserSocialDTO {

    @Size(min = 3, max = 64)
    @NotNull
    private String username;

    private String email;

    @Size(min = 3, max = 32)
    @NotNull
    private String vkId;

    @NotNull
    private String socialToken;


    private String firstName;


    private String lastName;


    private String photo;


    private String dateOfBirth;


    private String city;


    private String country;

    @NotNull
    private Sex sex;

    @NotNull
    private Set<String> roles;
}
