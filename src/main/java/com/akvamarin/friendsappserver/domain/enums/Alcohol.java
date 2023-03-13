package com.akvamarin.friendsappserver.domain.enums;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public enum Alcohol implements Serializable {
    FOR_COMPANY("за компанию"),
    NEVER("никогда"),
    OFTEN("часто"),
    RARELY("редко"),
    NO_ANSWER("нет ответа");

    private final String rusValue;

    //@JsonValue  //сериализует весь объект (object ==> json) (для БД)
    @Override
    public String toString() {
        return rusValue;
    }

    public int getNumberValue(){
        return switch (this) {
            case FOR_COMPANY -> 1;
            case OFTEN -> 2;
            case NEVER -> 3;
            case RARELY -> 4;
            default -> 0;
        };
    }
}
