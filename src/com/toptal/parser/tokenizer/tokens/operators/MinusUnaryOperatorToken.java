package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class MinusUnaryOperatorToken extends UnaryOperatorToken {
    public MinusUnaryOperatorToken() {
        super("-", LinearPolynomialNode::negate);
    }
}
