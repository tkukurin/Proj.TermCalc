package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class DivisionBinaryOperatorToken extends BinaryOperatorToken {

    public static final char REPRESENTATION = '/';

    public DivisionBinaryOperatorToken() {
        super(Character.toString(REPRESENTATION), LinearPolynomialNode::divide);
    }
}
