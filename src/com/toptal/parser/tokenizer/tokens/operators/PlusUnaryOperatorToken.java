package com.toptal.parser.tokenizer.tokens.operators;

import java.util.function.Function;

public class PlusUnaryOperatorToken extends UnaryOperatorToken {

    public PlusUnaryOperatorToken() {
        super("+", Function.identity());
    }

}
