package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class LogarithmOperatorToken extends UnaryOperatorToken {
    public LogarithmOperatorToken() {
        super("log", LinearPolynomialNode::logarithm);
    }
}
