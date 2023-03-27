package com.akvamarin.friendsappserver.domain.dto;

import com.akvamarin.friendsappserver.domain.entity.location.Country;
import com.akvamarin.friendsappserver.domain.entity.location.Region;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Builder
@Jacksonized
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO implements Serializable {
    private Long id;

    @NotBlank
    private String name;
    private double lat;
    private double lon;
    private Long regionID;

    @NotBlank
    private Long  countryID;

}
