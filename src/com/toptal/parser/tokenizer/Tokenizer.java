package com.toptal.parser.tokenizer;

import com.toptal.parser.QueryParseException;
import com.toptal.parser.tokenizer.tokens.*;
import com.toptal.parser.tokenizer.tokens.operators.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Tokenizer {

    private static final Map<Character, Supplier<Token>> singleCharacterToSupplier = new HashMap<>();

    static {
        singleCharacterToSupplier.put('+', AddOperatorToken::new);
        singleCharacterToSupplier.put('-', SubtractionOperatorToken::new);
        singleCharacterToSupplier.put('*', MultiplicationOperatorToken::new);
        singleCharacterToSupplier.put('/', DivisionOperatorToken::new);
        singleCharacterToSupplier.put('(', OpenParenthesisToken::new);
        singleCharacterToSupplier.put(')', CloseParenthesisToken::new);
        singleCharacterToSupplier.put('x', () -> new VariableToken("x"));
    }

    private Token currentToken;
    private int position;

    private final String input;
    private final List<StringToTokenConverter> stringToTokenConverters;

    public Tokenizer(String input) {
        this.input = input;
        this.position = 0;
        this.currentToken = new StartToken();
        this.stringToTokenConverters = createTokenConverters();
    }

    public Token next() {

        while (position < input.length() && Character.isWhitespace(currentCharacter())) {
            position++;
        }

        if (position == input.length()) {
            return new EofToken();
        }

        for (StringToTokenConverter stringToTokenConverter : stringToTokenConverters) {
            if (stringToTokenConverter.accepts(currentCharacter())) {
                int endingPos = stringToTokenConverter.findEndingPosition(input, position);
                String representation = input.substring(position, endingPos);

                this.currentToken = stringToTokenConverter.createToken(representation);
                this.position = endingPos;

                return this.currentToken;
            }
        }

        throw new QueryParseException("Unexpected character encountered: " + currentCharacter());
    }

    private LinkedList<StringToTokenConverter> createTokenConverters() {
        LinkedList<StringToTokenConverter> stringToTokenConverters = new LinkedList<>();

        stringToTokenConverters.add(new StringToTokenConverter(
                this::isStartOfDouble,
                this::advancePositionToNextNonDoubleCharacter,
                NumberToken::new));

        stringToTokenConverters.add(new StringToTokenConverter(
                this::isImplicitMultiplicationSign,
                (input, position) -> position,
                s -> new MultiplicationOperatorToken()));

        stringToTokenConverters.add(new StringToTokenConverter(
                singleCharacterToSupplier::containsKey,
                (input, position) -> position + 1,
                s -> singleCharacterToSupplier.get(s.charAt(0)).get()));

        stringToTokenConverters.add(new StringToTokenConverter(
                c -> c == 'l',
                (input, position) -> position + "log".length(),
                s -> new LogarithmOperatorToken()));

        return stringToTokenConverters;
    }

    private int remainAtCurrentPosition(String input, int position) {
        return position;
    }

    private int advancePositionByOne(String input, int position) {
        return position + 1;
    }

    private boolean isStartOfDouble(char c) {
        return Character.isDigit(c) || c == '.';
    }

    private int advancePositionToNextNonDoubleCharacter(String input, int position) {
        while (position < input.length() && isStartOfDouble(input.charAt(position))) {
            position++;
        }

        return position;
    }

    private boolean isLogarithmString(String input, int position) {
        return input.length() >= position + "og".length()
                && input.charAt(position + 1) == 'o'
                && input.charAt(position + 2) == 'g';
    }

    private boolean isImplicitMultiplicationSign(char current) {
        boolean lastTokenCanBeMultiplied = currentToken.getClass() == NumberToken.class ||
                currentToken.getClass() == CloseParenthesisToken.class ||
                currentToken.getClass() == VariableToken.class;

        return (current == '(' || current == 'x') && lastTokenCanBeMultiplied
                || (Character.isDigit(current) && currentToken.getClass() == CloseParenthesisToken.class);
    }

    private char currentCharacter() {
        return input.charAt(position);
    }

}
