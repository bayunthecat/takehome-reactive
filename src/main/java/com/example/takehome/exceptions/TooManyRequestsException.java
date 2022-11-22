package com.example.takehome.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS)
public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException() {
    }

    public TooManyRequestsException(String message) {
        super(message);
    }
}
