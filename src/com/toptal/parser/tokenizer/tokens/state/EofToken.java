package com.toptal.parser.tokenizer.tokens.state;

import com.toptal.parser.tokenizer.tokens.Token;

public class EofToken extends Token {
    public EofToken() {
        super(true, null);
    }
}
