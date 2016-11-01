package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class SubtractionBinaryOperatorToken extends BinaryOperatorToken {

    public static final char REPRESENTATION = '-';

    public SubtractionBinaryOperatorToken() {
        super(Character.toString(REPRESENTATION), LinearPolynomialNode::subtract);
    }
}
