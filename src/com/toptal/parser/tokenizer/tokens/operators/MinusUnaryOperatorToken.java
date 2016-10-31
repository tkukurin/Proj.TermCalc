package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class MinusUnaryOperatorToken extends UnaryOperatorToken {

    public static final char REPRESENTATION = '-';

    public MinusUnaryOperatorToken() {
        super("-", LinearPolynomialNode::negate);
    }
}
