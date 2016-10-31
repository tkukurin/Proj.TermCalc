package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class LogarithmFunctionToken extends FunctionToken {

    public static final String REPRESENTATION = "log";

    public LogarithmFunctionToken() {
        super(REPRESENTATION, LinearPolynomialNode::logarithm);
    }
}
