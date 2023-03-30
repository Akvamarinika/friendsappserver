package com.akvamarin.friendsappserver.domain.dto.responseerror;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Класс определяет структуру ответа при запросе
 * на ошибки валидации сущностей
 * */

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ValidationErrorResponse
        extends ErrorResponse {
    private final List<ErrorDescription> errors;

    public ValidationErrorResponse(int statusCode, String message, List<ErrorDescription> errors) {
        super(statusCode, message);
        this.errors = errors;
    }
}