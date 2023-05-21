package com.akvamarin.friendsappserver.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Builder
@Jacksonized
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserCoords implements Serializable {
    private Double lat;

    private Double lon;
}
