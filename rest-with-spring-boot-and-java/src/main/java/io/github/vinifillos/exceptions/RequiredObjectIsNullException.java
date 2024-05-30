package io.github.vinifillos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectIsNullException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public RequiredObjectIsNullException(String message) {
        super(message);
    }

    public RequiredObjectIsNullException() {
        super("It's not allowed to persist a null object!");
    }
}