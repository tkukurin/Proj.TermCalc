package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class AdditionBinaryOperatorToken extends BinaryOperatorToken {

    public static final char REPRESENTATION = '+';

    public AdditionBinaryOperatorToken() {
        super(Character.toString(REPRESENTATION), LinearPolynomialNode::add);
    }
}
