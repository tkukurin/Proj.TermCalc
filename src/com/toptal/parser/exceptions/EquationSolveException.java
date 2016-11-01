package com.toptal.parser.exceptions;

public class EquationSolveException extends RuntimeException {

    public EquationSolveException(String message) {
        super(message);
    }

    public EquationSolveException(String message, Throwable cause) {
        super(message, cause);
    }

}
