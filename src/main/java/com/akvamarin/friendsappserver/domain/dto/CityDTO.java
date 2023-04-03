package com.akvamarin.friendsappserver.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
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

    private Long federalDistrictID;

    private String federalDistrictName;

    private Long regionID;

    private String regionName;

    @NotBlank
    private Long countryID;


    private String countryName;

}
