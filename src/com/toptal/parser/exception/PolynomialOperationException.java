package com.toptal.parser.exception;

public class PolynomialOperationException extends RuntimeException {

    public PolynomialOperationException() {
    }

    public PolynomialOperationException(String s) {
        super(s);
    }

    public PolynomialOperationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PolynomialOperationException(Throwable throwable) {
        super(throwable);
    }

    public PolynomialOperationException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
