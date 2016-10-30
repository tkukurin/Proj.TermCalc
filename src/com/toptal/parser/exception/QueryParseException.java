package com.toptal.parser.exception;

public class QueryParseException extends RuntimeException {

    public QueryParseException() {
    }

    public QueryParseException(String s) {
        super(s);
    }

    public QueryParseException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
