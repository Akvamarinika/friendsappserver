package com.akvamarin.friendsappserver.domain.enums;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public enum Partner implements Serializable {
    MAN("парень"),
    WOMAN("девушка"),
    COMPANY("компания"),
    ANY("любой"),
    UNKNOWN("неизвестно");

    private final String rusValue;

    @Override
    public String toString() {
        return rusValue;
    }

    public int getNumberValue(){
        return switch (this) {
            case MAN -> 1;
            case WOMAN -> 2;
            case COMPANY -> 3;
            case ANY -> 4;
            default -> 0;
        };
    }
}
