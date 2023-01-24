package com.example.authorization.enums;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public enum PeriodOfTime implements Serializable {
    MORNING("утро"),
    AFTERNOON("день"),
    EVENING("вечер"),
    NIGHT("ночь"),
    UNKNOWN("неизвестно");

    private final String rusValue;

    @Override
    public String toString() {
        return rusValue;
    }

    public int getNumberValue(){
        return switch (this) {
            case MORNING -> 1;
            case AFTERNOON -> 2;
            case EVENING -> 3;
            case NIGHT -> 4;
            default -> 0;
        };
    }
}
