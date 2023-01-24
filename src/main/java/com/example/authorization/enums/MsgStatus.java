package com.example.authorization.enums;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public enum MsgStatus implements Serializable {
    VIEWED("просмотрено"),
    DELIVERED("доставлено"),
    NOT_SENT("не отправлено"),
    DELETED("удалено"),
    EDITED("изменено"),
    UNKNOWN("хз");

    private final String rusValue;

    @Override
    public String toString() {
        return rusValue;
    }

    public int getNumberValue(){
        return switch (this) {
            case VIEWED -> 1;
            case DELIVERED -> 2;
            case NOT_SENT -> 3;
            case DELETED -> 4;
            case EDITED -> 5;
            default -> 0;
        };
    }
}
