package com.akvamarin.friendsappserver.domain.dto;

import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.enums.*;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    private Long id;

    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Invalid mail format")
    private String email;

    @Pattern(regexp = "^\\d{11}$", message = "Invalid phone format")
    @Size(min = 11, max = 11, message = "Phone number must be 11 characters")
    private String phone;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 128, message = "Password must be between 6-128 characters")
    private String password;

    private String passwordConfirm;

    private String passwordToChange;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirthday;

    @NotBlank(message = "Nickname cannot be blank")
    @Size(min = 2, max = 50, message = "Nickname must be between 2-50 characters")
    private String nickname;

    //@Enumerated(EnumType.STRING)
    private Sex sex;

    private String aboutMe;

    //@Enumerated(EnumType.STRING)
    private Smoking smoking;

    //@Enumerated(EnumType.STRING)
    private Alcohol alcohol;

    // @Enumerated(EnumType.STRING)
    private Psychotype psychotype;

    @NotBlank(message = "URL avatar cannot be blank")
    private String urlAvatar;

    //@NotBlank
    private City city;

    //@Enumerated(EnumType.STRING)
    // private Set<Role> roles;

    //@Enumerated(EnumType.STRING)
    private Roles role;

    private String vkId;

}
