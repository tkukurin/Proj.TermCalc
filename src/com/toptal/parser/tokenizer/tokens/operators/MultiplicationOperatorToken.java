package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class MultiplicationOperatorToken extends BinaryOperatorToken {
    public MultiplicationOperatorToken() {
        super("*", LinearPolynomialNode::mutliply);
    }
}
