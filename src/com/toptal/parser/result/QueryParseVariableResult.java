package com.toptal.parser.result;

public class QueryParseVariableResult extends QueryParseResult {
    public QueryParseVariableResult(double solution) {
        super(solution);
    }

    @Override
    public String toString() {
        return "x = " + getSolution();
    }
}
