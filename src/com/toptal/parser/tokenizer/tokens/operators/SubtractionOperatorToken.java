package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

public class SubtractionOperatorToken extends BinaryOperatorToken {

    public static final char REPRESENTATION = '-';

    public SubtractionOperatorToken() {
        super(Character.toString(REPRESENTATION), LinearPolynomialNode::subtract);
    }
}
