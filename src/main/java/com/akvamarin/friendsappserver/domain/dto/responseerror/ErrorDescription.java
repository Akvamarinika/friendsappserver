package com.akvamarin.friendsappserver.domain.dto.responseerror;

import lombok.Data;

@Data
public class ErrorDescription {
    private final String field;
    private final String error;
}