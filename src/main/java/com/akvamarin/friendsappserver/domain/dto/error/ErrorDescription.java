package com.akvamarin.friendsappserver.domain.dto.error;

import lombok.Data;

@Data
public class ErrorDescription {
    private final String field;
    private final String error;
}