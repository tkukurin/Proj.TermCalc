package com.toptal;

import com.toptal.parser.exceptions.QueryParseException;
import com.toptal.parser.tokenizer.TokenizerStateMapper;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.operators.*;
import com.toptal.parser.tokenizer.tokens.state.CloseParenthesisToken;
import com.toptal.parser.tokenizer.tokens.state.OpenParenthesisToken;
import com.toptal.parser.tokenizer.tokens.state.StartToken;
import com.toptal.parser.tokenizer.tokens.values.NumberToken;
import com.toptal.parser.tokenizer.tokens.values.VariableToken;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultTokenizerBundle {

    public static LinkedList<TokenizerStateMapper> createTokenConverters() {
        LinkedList<TokenizerStateMapper> tokenizerStateMappers = new LinkedList<>();

        tokenizerStateMappers.add(unaryPlusParser());
        tokenizerStateMappers.add(unaryMinusParser());
        tokenizerStateMappers.add(functionMustBeFollowedByParenthesisParser());
        tokenizerStateMappers.add(implicitMultiplicationParser());
        tokenizerStateMappers.add(doubleValueParser());
        tokenizerStateMappers.add(logarithmStringParser());
        tokenizerStateMappers.add(premappedCharacterParser());

        return tokenizerStateMappers;
    }

    private static TokenizerStateMapper unaryPlusParser() {
        return new TokenizerStateMapper(
                (currentCharacter, currentToken) -> currentCharacter == PlusUnaryOperatorToken.REPRESENTATION && precedesUnaryOperator(currentToken),
                (input, position) -> position + 1,
                (stringRepresentation) -> new PlusUnaryOperatorToken());
    }

    private static TokenizerStateMapper unaryMinusParser() {
        return new TokenizerStateMapper(
                (currentCharacter, currentToken) -> currentCharacter == MinusUnaryOperatorToken.REPRESENTATION && precedesUnaryOperator(currentToken),
                (input, position) -> position + 1,
                (stringRepresentation) -> new MinusUnaryOperatorToken());
    }

    private static boolean precedesUnaryOperator(Token currentToken) {
        Class<?> currentTokenType = currentToken.getClass();
        return currentTokenType == OpenParenthesisToken.class
                || currentTokenType == StartToken.class
                || currentTokenType.getSuperclass() == BinaryOperatorToken.class;
    }

    private static TokenizerStateMapper functionMustBeFollowedByParenthesisParser() {
        return new TokenizerStateMapper(
                (currentCharacter, currentToken) -> isFunctionToken(currentToken),
                (input, position) -> position + 1,
                (stringRepresentation) -> {
                    if(!stringRepresentation.equals(Character.toString(OpenParenthesisToken.REPRESENTATION))) {
                        throw new QueryParseException("Expecting function parameters to be parenthesised");
                    }

                    return new OpenParenthesisToken();
                });
    }

    private static boolean isFunctionToken(Token currentToken) {
        return currentToken instanceof FunctionToken;
    }

    private static TokenizerStateMapper implicitMultiplicationParser() {
        return new TokenizerStateMapper(
                DefaultTokenizerBundle::isImplicitMultiplicationSign,
                (input, position) -> position,
                (stringRepresentation) -> new MultiplicationBinaryOperatorToken());
    }

    private static boolean isImplicitMultiplicationSign(char currentCharacter, Token currentToken) {
        boolean lastTokenCanBeMultiplied = currentToken instanceof NumberToken ||
                currentToken instanceof CloseParenthesisToken ||
                currentToken instanceof VariableToken;

        final char startOfLogarithm = LogarithmFunctionToken.REPRESENTATION.charAt(0);
        return (currentCharacter == startOfLogarithm
                || currentCharacter == OpenParenthesisToken.REPRESENTATION
                || currentCharacter == VariableToken.REPRESENTATION)
                    && lastTokenCanBeMultiplied
                || (Character.isDigit(currentCharacter) && currentToken instanceof CloseParenthesisToken);
    }

    private static TokenizerStateMapper doubleValueParser() {
        return new TokenizerStateMapper(
                (currentCharacter, currentToken) -> isValidDoubleCharacter(currentCharacter),
                (input, position) -> advancePositionToNextNonDoubleCharacter(input, position + 1),
                NumberToken::new);
    }

    private static boolean isValidDoubleCharacter(char c) {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        char localeDecimalSeparator = symbols.getDecimalSeparator();

        return Character.isDigit(c) || c == localeDecimalSeparator;
    }

    private static int advancePositionToNextNonDoubleCharacter(String input, int position) {
        while (position < input.length() && isValidDoubleCharacter(input.charAt(position))) {
            position++;
        }

        return position;
    }

    private static TokenizerStateMapper logarithmStringParser() {
        return new TokenizerStateMapper(
                (currentCharacter, currentToken) -> currentCharacter == LogarithmFunctionToken.REPRESENTATION.charAt(0),
                (input, position) -> {
                    requireLogarithmString(input, position);
                    return position + LogarithmFunctionToken.REPRESENTATION.length();
                },
                (stringRepresentation) -> new LogarithmFunctionToken());
    }

    private static void requireLogarithmString(String input, Integer position) {
        if(!isLogarithmString(input, position)) {
            throw new QueryParseException("Found 'l' character; did you mean: 'log'?");
        }
    }

    private static boolean isLogarithmString(String input, int position) {
        return input.length() >= position + LogarithmFunctionToken.REPRESENTATION.length()
                && input.charAt(position + 1) == LogarithmFunctionToken.REPRESENTATION.charAt(1)
                && input.charAt(position + 2) == LogarithmFunctionToken.REPRESENTATION.charAt(2);
    }

    private static TokenizerStateMapper premappedCharacterParser() {

        final Map<Character, Supplier<Token>> singleCharacterToSupplier = new HashMap<>();

        singleCharacterToSupplier.put(AdditionBinaryOperatorToken.REPRESENTATION, AdditionBinaryOperatorToken::new);
        singleCharacterToSupplier.put(SubtractionBinaryOperatorToken.REPRESENTATION, SubtractionBinaryOperatorToken::new);
        singleCharacterToSupplier.put(MultiplicationBinaryOperatorToken.REPRESENTATION, MultiplicationBinaryOperatorToken::new);
        singleCharacterToSupplier.put(DivisionBinaryOperatorToken.REPRESENTATION, DivisionBinaryOperatorToken::new);
        singleCharacterToSupplier.put(OpenParenthesisToken.REPRESENTATION, OpenParenthesisToken::new);
        singleCharacterToSupplier.put(CloseParenthesisToken.REPRESENTATION, CloseParenthesisToken::new);
        singleCharacterToSupplier.put(VariableToken.REPRESENTATION, VariableToken::new);

        return new TokenizerStateMapper(
                (currentCharacter, currentToken) -> singleCharacterToSupplier.containsKey(currentCharacter),
                (input, position) -> position + 1,
                (stringRepresentation) -> singleCharacterToSupplier.get(stringRepresentation.charAt(0)).get());
    }

}
