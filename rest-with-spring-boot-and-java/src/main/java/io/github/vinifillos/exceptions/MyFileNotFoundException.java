package io.github.vinifillos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyFileNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public MyFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyFileNotFoundException(String message) {
        super(message);
    }
}
