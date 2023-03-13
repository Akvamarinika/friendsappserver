package com.akvamarin.friendsappserver.domain.enums;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public enum Sex implements Serializable {
    MALE("парень"),
    FEMALE("девушка"),
    NO_ANSWER("нет ответа");

    private final String rusValue;

    @Override
    public String toString() {
        return rusValue;
    }

    public int getNumberValue(){
        return switch (this) {
            case MALE -> 1;
            case FEMALE -> 2;
            default -> 0;
        };
    }
}
