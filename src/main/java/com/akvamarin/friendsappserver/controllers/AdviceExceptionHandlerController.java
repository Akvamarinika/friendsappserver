package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.dto.error.ErrorDescription;
import com.akvamarin.friendsappserver.domain.dto.error.ErrorResponse;
import com.akvamarin.friendsappserver.domain.dto.error.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.List;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


/**
 * Контроллер, отвечающий за обработку ошибок на нескольких контроллерах.
 *
 * @see UserRestController
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
        return new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), buildMessage(bindingResult), buildErrors(bindingResult));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) //400, при ошибке собственной валидации
    @ExceptionHandler(ValidationException.class)
    public ErrorResponse badRequest(ValidationException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse conflict(IllegalArgumentException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) //404, когда сущность в БД не найдена
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse notFound(EntityNotFoundException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

   /* @ResponseStatus(HttpStatus.NO_CONTENT) //204, когда в БД удалена запись
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse error(EntityNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }*/

    @ResponseStatus(HttpStatus.FORBIDDEN) // 401, ресурс недоступен для данного типа пользователей
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException exception) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse error(RuntimeException exception) {
        log.error("", exception);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
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
