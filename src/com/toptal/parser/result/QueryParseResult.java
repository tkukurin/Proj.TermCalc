package com.toptal.parser.result;

public abstract class QueryParseResult {

    private final double solution;

    public QueryParseResult(double solution) {
        this.solution = solution;
    }

    public double getSolution() {
        return solution;
    }

}
