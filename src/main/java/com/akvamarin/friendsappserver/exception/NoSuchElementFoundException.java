package com.akvamarin.friendsappserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchElementFoundException extends EntityNotFoundException {
    public NoSuchElementFoundException(String message) {
        super(message);
    }
}
