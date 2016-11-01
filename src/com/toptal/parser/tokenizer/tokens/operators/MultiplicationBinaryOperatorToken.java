package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class MultiplicationBinaryOperatorToken extends BinaryOperatorToken {

    public static final char REPRESENTATION = '*';

    public MultiplicationBinaryOperatorToken() {
        super(Character.toString(REPRESENTATION), LinearPolynomialNode::mutliply);
    }
}
