package com.toptal.parser.result;

public class QueryParseNumericResult extends QueryParseResult {
    public QueryParseNumericResult(double solution) {
        super(solution);
    }

    @Override
    public String toString() {
        return Double.toString(getSolution());
    }
}
