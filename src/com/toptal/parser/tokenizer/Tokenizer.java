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
        singleCharacterToSupplier.put('x', VariableToken::new);
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

    private LinkedList<StringToTokenConverter> createTokenConverters() {
        LinkedList<StringToTokenConverter> stringToTokenConverters = new LinkedList<>();

        stringToTokenConverters.add(unaryMinusParser());
        stringToTokenConverters.add(doubleValueParser());
        stringToTokenConverters.add(implicitMultiplicationParser());
        stringToTokenConverters.add(logarithmStringParser());
        stringToTokenConverters.add(premappedCharacterParser());

        return stringToTokenConverters;
    }

    private StringToTokenConverter unaryMinusParser() {
        return new StringToTokenConverter(
                (currentCharacter, currentToken) -> currentCharacter == '-' && (isNotNumberToken(currentToken)),
                (input, position) -> position + 1,
                (stringRepresentation) -> new MinusUnaryOperatorToken());
    }

    private StringToTokenConverter doubleValueParser() {
        return new StringToTokenConverter(
                (currentCharacter, currentToken) -> this.isValidDoubleCharacter(currentCharacter),
                (input, position) -> advancePositionToNextNonDoubleCharacter(input, position + 1),
                NumberToken::new);
    }

    private StringToTokenConverter implicitMultiplicationParser() {
        return new StringToTokenConverter(
                this::isImplicitMultiplicationSign,
                (input, position) -> position,
                (stringRepresentation) -> new MultiplicationOperatorToken());
    }

    private StringToTokenConverter logarithmStringParser() {
        return new StringToTokenConverter(
                (currentCharacter, currentToken) -> currentCharacter == 'l',
                (input, position) -> {
                    requireLogarithmString(input, position);
                    return position + "log".length();
                },
                (stringRepresentation) -> new LogarithmOperatorToken());
    }

    private StringToTokenConverter premappedCharacterParser() {
        return new StringToTokenConverter(
                (currentCharacter, currentToken) -> singleCharacterToSupplier.containsKey(currentCharacter),
                (input, position) -> position + 1,
                (stringRepresentation) -> singleCharacterToSupplier.get(stringRepresentation.charAt(0)).get());
    }

    private boolean isNotNumberToken(Token currentToken) {
        Class<?> currentTokenType = currentToken.getClass();
        return currentTokenType == OpenParenthesisToken.class
                || currentTokenType == StartToken.class
                || currentTokenType.getSuperclass() == BinaryOperatorToken.class;
    }

    private int advancePositionToNextNonDoubleCharacter(String input, int position) {
        while (position < input.length() && isValidDoubleCharacter(input.charAt(position))) {
            position++;
        }

        return position;
    }

    private boolean isValidDoubleCharacter(char c) {
        return Character.isDigit(c) || c == '.';
    }

    private void requireLogarithmString(String input, Integer position) {
        if(!isLogarithmString(input, position)) {
            throw new QueryParseException("Found 'l' character; did you mean: 'log'?");
        }
    }

    private boolean isLogarithmString(String input, int position) {
        return input.length() >= position + "og".length()
                && input.charAt(position + 1) == 'o'
                && input.charAt(position + 2) == 'g';
    }

    private boolean isImplicitMultiplicationSign(char current, Token currentToken) {
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
