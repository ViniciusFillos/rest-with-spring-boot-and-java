package io.github.vinifillos.exceptions;

import java.io.Serializable;
import java.util.Date;

public class ExceptionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date timestanp;
    private String message;
    private String details;

    public ExceptionResponse(Date timestanp, String message, String details) {
        this.timestanp = timestanp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestanp() {
        return timestanp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
