package com.akvamarin.friendsappserver.domain.dto;


import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Data
public class AuthServerToken {
    private String token;
    private String tokenType = "Bearer";

    public AuthServerToken(String token) {
        this.token = token;
    }
}
