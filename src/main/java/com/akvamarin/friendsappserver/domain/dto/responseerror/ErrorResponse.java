package com.akvamarin.friendsappserver.domain.dto.responseerror;

import lombok.Data;

@Data
public class ErrorResponse {
    private final int statusCode;
    private final String message;
}
