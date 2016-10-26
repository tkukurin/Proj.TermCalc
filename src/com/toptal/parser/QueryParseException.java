package com.toptal.parser;

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
