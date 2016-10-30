package com.toptal;

import com.toptal.parser.exception.QueryParseException;
import com.toptal.parser.tokenizer.StringToTokenConverter;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.operators.*;
import com.toptal.parser.tokenizer.tokens.state.CloseParenthesisToken;
import com.toptal.parser.tokenizer.tokens.state.OpenParenthesisToken;
import com.toptal.parser.tokenizer.tokens.state.StartToken;
import com.toptal.parser.tokenizer.tokens.values.NumberToken;
import com.toptal.parser.tokenizer.tokens.values.VariableToken;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultTokenizerBundle {

    public static LinkedList<StringToTokenConverter> createTokenConverters() {
        LinkedList<StringToTokenConverter> stringToTokenConverters = new LinkedList<>();

        stringToTokenConverters.add(unaryPlusParser());
        stringToTokenConverters.add(unaryMinusParser());
        stringToTokenConverters.add(implicitMultiplicationParser());
        stringToTokenConverters.add(doubleValueParser());
        stringToTokenConverters.add(logarithmStringParser());
        stringToTokenConverters.add(premappedCharacterParser());

        return stringToTokenConverters;
    }

    private static StringToTokenConverter unaryPlusParser() {
        return new StringToTokenConverter(
                (currentCharacter, currentToken) -> currentCharacter == '+' && isNotNumberToken(currentToken),
                (input, position) -> position + 1,
                (stringRepresentation) -> new PlusUnaryOperatorToken());
    }

    private static StringToTokenConverter unaryMinusParser() {
        return new StringToTokenConverter(
                (currentCharacter, currentToken) -> currentCharacter == '-' && isNotNumberToken(currentToken),
                (input, position) -> position + 1,
                (stringRepresentation) -> new MinusUnaryOperatorToken());
    }

    private static  StringToTokenConverter doubleValueParser() {
        return new StringToTokenConverter(
                (currentCharacter, currentToken) -> isValidDoubleCharacter(currentCharacter),
                (input, position) -> advancePositionToNextNonDoubleCharacter(input, position + 1),
                NumberToken::new);
    }

    private static  StringToTokenConverter implicitMultiplicationParser() {
        return new StringToTokenConverter(
                DefaultTokenizerBundle::isImplicitMultiplicationSign,
                (input, position) -> position,
                (stringRepresentation) -> new MultiplicationOperatorToken());
    }

    private static  StringToTokenConverter logarithmStringParser() {
        return new StringToTokenConverter(
                (currentCharacter, currentToken) -> currentCharacter == 'l',
                (input, position) -> {
                    requireLogarithmString(input, position);
                    return position + "log".length();
                },
                (stringRepresentation) -> new LogarithmOperatorToken());
    }

    private static  StringToTokenConverter premappedCharacterParser() {

        final Map<Character, Supplier<Token>> singleCharacterToSupplier = new HashMap<>();

        singleCharacterToSupplier.put('+', AdditionOperatorToken::new);
        singleCharacterToSupplier.put('-', SubtractionOperatorToken::new);
        singleCharacterToSupplier.put('*', MultiplicationOperatorToken::new);
        singleCharacterToSupplier.put('/', DivisionOperatorToken::new);
        singleCharacterToSupplier.put('(', OpenParenthesisToken::new);
        singleCharacterToSupplier.put(')', CloseParenthesisToken::new);
        singleCharacterToSupplier.put('x', VariableToken::new);

        return new StringToTokenConverter(
                (currentCharacter, currentToken) -> singleCharacterToSupplier.containsKey(currentCharacter),
                (input, position) -> position + 1,
                (stringRepresentation) -> singleCharacterToSupplier.get(stringRepresentation.charAt(0)).get());
    }

    private static boolean isNotNumberToken(Token currentToken) {
        Class<?> currentTokenType = currentToken.getClass();
        return currentTokenType == OpenParenthesisToken.class
                || currentTokenType == StartToken.class
                || currentTokenType.getSuperclass() == BinaryOperatorToken.class;
    }

    private static int advancePositionToNextNonDoubleCharacter(String input, int position) {
        while (position < input.length() && isValidDoubleCharacter(input.charAt(position))) {
            position++;
        }

        return position;
    }

    private static boolean isValidDoubleCharacter(char c) {
        return Character.isDigit(c) || c == '.';
    }

    private static void requireLogarithmString(String input, Integer position) {
        if(!isLogarithmString(input, position)) {
            throw new QueryParseException("Found 'l' character; did you mean: 'log'?");
        }
    }

    private static boolean isLogarithmString(String input, int position) {
        return input.length() >= position + "og".length()
                && input.charAt(position + 1) == 'o'
                && input.charAt(position + 2) == 'g';
    }

    private static boolean isImplicitMultiplicationSign(char current, Token currentToken) {
        boolean lastTokenCanBeMultiplied = currentToken.getClass() == NumberToken.class ||
                currentToken.getClass() == CloseParenthesisToken.class ||
                currentToken.getClass() == VariableToken.class;

        return (current == '(' || current == 'x') && lastTokenCanBeMultiplied
                || (Character.isDigit(current) && currentToken.getClass() == CloseParenthesisToken.class);
    }

}
