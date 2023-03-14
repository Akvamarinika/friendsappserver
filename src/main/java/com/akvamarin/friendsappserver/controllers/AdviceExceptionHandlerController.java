package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.responseerror.ErrorDescription;
import com.akvamarin.friendsappserver.domain.responseerror.ErrorResponse;
import com.akvamarin.friendsappserver.domain.responseerror.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


/**
 * Контроллер, отвечающий за обработку ошибок на нескольких контроллерах.
 *
 * @see UserApiController
 *
 *
 * */

@Slf4j
@Hidden //скрыть контроллер для Swagger
@RestControllerAdvice(annotations = RestController.class)
public class AdviceExceptionHandlerController {

    /*Экземпляр exception и request можно получить через аргументы методов. */
    /*@ExceptionHandler указывает, какой тип исключения мы хотим обработать.*/
    /*MethodArgumentNotValidException, исключение от Jakarta Bean Validation / Hibernate Validator */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorResponse badRequest(MethodArgumentNotValidException exception) {
        final BindingResult bindingResult = exception.getBindingResult();
        return new ValidationErrorResponse(buildMessage(bindingResult), buildErrors(bindingResult));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) //400, при ошибке собственной валидации
    @ExceptionHandler(ValidationException.class)
    public ErrorResponse badRequest(ValidationException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) //404, когда сущность в БД не найдена
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse error(EntityNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse error(RuntimeException exception) {
        log.error("", exception);
        return new ErrorResponse(exception.getMessage());
    }

    private String buildMessage(BindingResult bindingResult) {
        return String.format("Error on %s, rejected errors [%s]",
                bindingResult.getTarget(),
                bindingResult.getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(joining(";")));
    }

    private List<ErrorDescription> buildErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(e -> new ErrorDescription(e.getField(), e.getDefaultMessage()))
                .collect(toList());
    }
}
