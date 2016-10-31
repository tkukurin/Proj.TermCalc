package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class DivisionOperatorToken extends BinaryOperatorToken {

    public static final char REPRESENTATION = '/';

    public DivisionOperatorToken() {
        super(Character.toString(REPRESENTATION), LinearPolynomialNode::divide);
    }
}
