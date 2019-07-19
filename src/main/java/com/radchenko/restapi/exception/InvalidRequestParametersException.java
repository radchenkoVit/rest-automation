package com.radchenko.restapi.exception;

public class InvalidRequestParametersException extends RuntimeException {

    public InvalidRequestParametersException() {
    }

    public InvalidRequestParametersException(String message) {
        super(message);
    }

    public InvalidRequestParametersException(String message, Throwable cause) {
        super(message, cause);
    }
}
