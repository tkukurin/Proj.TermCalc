package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class AdditionOperatorToken extends BinaryOperatorToken {
    public AdditionOperatorToken() {
        super("+", LinearPolynomialNode::add);
    }
}
