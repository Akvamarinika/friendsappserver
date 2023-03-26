package com.akvamarin.friendsappserver.domain.dto;

import com.akvamarin.friendsappserver.domain.enums.Alcohol;
import com.akvamarin.friendsappserver.domain.enums.Psychotype;
import com.akvamarin.friendsappserver.domain.enums.Sex;
import com.akvamarin.friendsappserver.domain.enums.Smoking;
import com.akvamarin.friendsappserver.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Builder @Jacksonized
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    private Long id;

    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = Constants.PATTERN_EMAIL, message = "Invalid mail format")
    private String email;

   // @Email
    @NotBlank(message = "Login cannot be blank")
    private String username;

    //@Pattern(regexp = "^\\d{11}$", message = "Invalid phone format")
    //@Size(min = 11, max = 11, message = "Phone number must be 11 characters")
    private String phone;

    //@NotBlank(message = "Password cannot be blank")
    //@Size(min = 6, max = 128, message = "Password must be between 6-128 characters")
    private String password;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirthday;

    @NotBlank(message = "Nickname cannot be blank")
    @Size(min = 2, max = 50, message = "Nickname must be between 2-50 characters")
    private String nickname;

    private Sex sex;

    private String aboutMe;

    private Smoking smoking;

    private Alcohol alcohol;

    private Psychotype psychotype;

    @NotBlank(message = "URL avatar cannot be blank")
    private String urlAvatar;

    private long cityId;

    private long countryId;

    private Set<String> roles;

    private String vkId;

}
