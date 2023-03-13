package com.akvamarin.friendsappserver.domain.responseerror;

import lombok.Data;

@Data
public class ErrorDescription {
    private final String field;
    private final String error;
}