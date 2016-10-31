package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class MultiplicationOperatorToken extends BinaryOperatorToken {

    public static final char REPRESENTATION = '*';

    public MultiplicationOperatorToken() {
        super(Character.toString(REPRESENTATION), LinearPolynomialNode::mutliply);
    }
}
