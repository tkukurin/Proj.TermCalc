package com.toptal.parser.tokenizer;

import com.toptal.parser.exceptions.QueryParseException;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.state.EofToken;

import java.util.List;

import static java.lang.Character.isWhitespace;

public class Tokenizer {

    private Token currentToken;
    private int position;

    private final String input;
    private final List<TokenizerStateMapper> tokenizerStateMappers;

    Tokenizer(String input,
              Token initialToken,
              List<TokenizerStateMapper> tokenizerStateMappers) {
        this.input = input;
        this.tokenizerStateMappers = tokenizerStateMappers;
        this.currentToken = initialToken;
        this.position = 0;
    }

    public Token next() {

        while (!isPastEndOfInput(position) && isWhitespace(currentCharacter())) {
            position++;
        }

        if (isPastEndOfInput(position)) {
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

    private boolean isPastEndOfInput(int position) {
        return position >= input.length();
    }

    private char currentCharacter() {
        return input.charAt(position);
    }

}
