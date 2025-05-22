package com.vrana.database.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OpenResourceNotFoundException extends RuntimeException {
    public OpenResourceNotFoundException(String message) {
        super(message);
    }
}
