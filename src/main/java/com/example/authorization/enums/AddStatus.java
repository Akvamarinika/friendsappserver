package com.example.authorization.enums;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public enum AddStatus implements Serializable {
    WAITING("в ожидании"),
    ADDED("добавлен"),
    REJECTED("отклонен"),
    DELETED("удален"),
    UNKNOWN("хз");

    private final String rusValue;

    @Override
    public String toString() {
        return rusValue;
    }

    public int getNumberValue(){
        return switch (this) {
            case WAITING -> 1;
            case ADDED -> 2;
            case REJECTED -> 3;
            case DELETED -> 4;
            default -> 0;
        };
    }

}


