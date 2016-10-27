package com.toptal.parser;

public class QueryParseResult {

    private final double solution;
    private final boolean isLinearEquation;

    public QueryParseResult(double solution, boolean isLinearEquation) {
        this.solution = solution;
        this.isLinearEquation = isLinearEquation;
    }

    public double getSolution() {
        return solution;
    }

    public boolean isLinearEquation() {
        return isLinearEquation;
    }
}
