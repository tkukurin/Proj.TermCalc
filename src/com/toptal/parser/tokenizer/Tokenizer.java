package com.toptal.parser.tokenizer;

import com.toptal.parser.exception.QueryParseException;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.state.EofToken;

import java.util.List;

public class Tokenizer {

    private Token currentToken;
    private int position;

    private final String input;
    private final List<StringToTokenConverter> stringToTokenConverters;

    public Tokenizer(String input,
                     Token initialToken,
                     List<StringToTokenConverter> stringToTokenConverters) {
        this.input = input;
        this.stringToTokenConverters = stringToTokenConverters;
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

        for (StringToTokenConverter stringToTokenConverter : stringToTokenConverters) {
            if (stringToTokenConverter.accepts(currentCharacter(), this.currentToken)) {
                int endingPosition = stringToTokenConverter.findEndingPosition(input, position);
                String representation = input.substring(position, endingPosition);

                this.currentToken = stringToTokenConverter.createToken(representation);
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
