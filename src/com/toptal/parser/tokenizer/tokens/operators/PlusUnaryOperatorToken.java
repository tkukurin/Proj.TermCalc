package com.toptal.parser.tokenizer.tokens.operators;

import java.util.function.Function;

public class PlusUnaryOperatorToken extends UnaryOperatorToken {

    public static final char REPRESENTATION = '+';

    public PlusUnaryOperatorToken() {
        super(Character.toString(REPRESENTATION), Function.identity());
    }

}
