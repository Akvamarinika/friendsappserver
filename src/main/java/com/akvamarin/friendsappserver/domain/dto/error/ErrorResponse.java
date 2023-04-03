package com.akvamarin.friendsappserver.domain.dto.error;

import lombok.Data;

@Data
public class ErrorResponse {
    private final int statusCode;
    private final String message;
}
