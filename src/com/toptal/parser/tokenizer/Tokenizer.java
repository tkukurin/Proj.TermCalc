package com.toptal.parser.tokenizer;

import com.toptal.parser.exception.QueryParseException;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.state.EofToken;

import java.util.List;

public class Tokenizer {

    private Token currentToken;
    private int position;

    private final String input;
    private final List<TokenizerStateMapper> tokenizerStateMappers;

    public Tokenizer(String input,
                     Token initialToken,
                     List<TokenizerStateMapper> tokenizerStateMappers) {
        this.input = input;
        this.tokenizerStateMappers = tokenizerStateMappers;
        this.currentToken = initialToken;
        this.position = 0;
    }

    public Token next() {

        while (position < input.length() && Character.isWhitespace(currentCharacter())) {
            position++;
        }

        if (position == input.length()) {
            return new EofToken();
        }

        for (TokenizerStateMapper tokenizerStateMapper : tokenizerStateMappers) {
            if (tokenizerStateMapper.accepts(currentCharacter(), this.currentToken)) {
                int endingPosition = tokenizerStateMapper.findEndingPosition(input, position);
                String representation = input.substring(position, endingPosition);

                this.currentToken = tokenizerStateMapper.createToken(representation);
                this.position = endingPosition;

                return this.currentToken;
            }
        }

        throw new QueryParseException("Unexpected character encountered: " + currentCharacter());
    }

    private char currentCharacter() {
        return input.charAt(position);
    }

}
