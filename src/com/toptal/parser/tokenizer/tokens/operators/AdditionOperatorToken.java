package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class AdditionOperatorToken extends BinaryOperatorToken {

    public static final char REPRESENTATION = '+';

    public AdditionOperatorToken() {
        super(Character.toString(REPRESENTATION), LinearPolynomialNode::add);
    }
}
