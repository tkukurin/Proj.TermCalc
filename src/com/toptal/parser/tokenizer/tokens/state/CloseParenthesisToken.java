package com.toptal.parser.tokenizer.tokens.state;

import com.toptal.parser.tokenizer.tokens.Token;

public class CloseParenthesisToken extends Token {

    public static final char REPRESENTATION = ')';

    public CloseParenthesisToken() {
        super(false, REPRESENTATION + "");
    }
}
