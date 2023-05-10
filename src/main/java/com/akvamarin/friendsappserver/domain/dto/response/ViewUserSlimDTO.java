package com.akvamarin.friendsappserver.domain.dto.response;

import com.akvamarin.friendsappserver.domain.dto.CityDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Jacksonized
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewUserSlimDTO {
    private Long id;

    private String username;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirthday;

    @NotBlank(message = "Nickname cannot be blank")
    @Size(min = 2, max = 50, message = "Nickname must be between 2-50 characters")
    private String nickname;

    @NotBlank(message = "URL avatar cannot be blank")
    private String urlAvatar;

    private CityDTO cityDTO;

    private Set<String> roles;

}
