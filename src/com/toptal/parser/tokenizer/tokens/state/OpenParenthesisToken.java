package com.toptal.parser.tokenizer.tokens.state;

import com.toptal.parser.tokenizer.tokens.Token;

public class OpenParenthesisToken extends Token {

    public static final char REPRESENTATION = '(';

    public OpenParenthesisToken() {
        super(false, REPRESENTATION + "");
    }
}
