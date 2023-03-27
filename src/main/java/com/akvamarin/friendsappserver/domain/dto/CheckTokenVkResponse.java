package com.akvamarin.friendsappserver.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckTokenVkResponse {
    private final int success;
    private final long date;
    private final int expire;
    private final long userId;
}
