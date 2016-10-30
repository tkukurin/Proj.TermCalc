package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class AddOperatorToken extends BinaryOperatorToken {
    public AddOperatorToken() {
        super("+", LinearPolynomialNode::add);
    }
}
