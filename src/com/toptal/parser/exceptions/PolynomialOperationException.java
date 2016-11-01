package com.toptal.parser.exceptions;

public class PolynomialOperationException extends RuntimeException {

    public PolynomialOperationException(String message) {
        super(message);
    }

    public PolynomialOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
