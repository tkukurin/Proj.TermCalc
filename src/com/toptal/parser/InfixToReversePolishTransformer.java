package com.toptal.parser;

import java.util.Stack;

public class InfixToReversePolishTransformer {

    private Stack<String> values;
    private Stack<String> operators;

    public InfixToReversePolishTransformer() {
        this.values = new Stack<>();
        this.operators = new Stack<>();
    }

    public static QueryParseResult parse(String query) {
        return null;
    }

    // also should parse "23x", "x23"
    private boolean isVariable(String s) {
        return "x".equals(s);
    }

}
