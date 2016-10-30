package com.toptal;

import com.toptal.environment.CalculatorEnvironment;
import com.toptal.parser.InfixToReversePolishTransformer;
import com.toptal.parser.InfixTransformer;
import com.toptal.parser.LinearEquationSolver;
import com.toptal.parser.QueryParser;
import com.toptal.parser.exception.QueryParseException;
import com.toptal.parser.tokenizer.StringToTokenConverter;
import com.toptal.parser.tokenizer.TokenizerFactory;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.operators.*;
import com.toptal.parser.tokenizer.tokens.state.CloseParenthesisToken;
import com.toptal.parser.tokenizer.tokens.state.OpenParenthesisToken;
import com.toptal.parser.tokenizer.tokens.state.StartToken;
import com.toptal.parser.tokenizer.tokens.values.NumberToken;
import com.toptal.parser.tokenizer.tokens.values.VariableToken;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.function.Supplier;

public class ApplicationEntry {

    public static void main(String[] args) throws IOException {
        CalculatorEnvironment environment = new CalculatorEnvironment(">", new Scanner(System.in), new OutputStreamWriter(System.out));

        List<StringToTokenConverter> stringToTokenConverters = createTokenConverters();
        TokenizerFactory tokenizerFactory = new TokenizerFactory(stringToTokenConverters, StartToken::new);

        Map<Class<? extends Token>, InfixTransformer> tokenParsingMap = createTokenParsingMap();
        InfixToReversePolishTransformer infixToReversePolishTransformer =
                new InfixToReversePolishTransformer(tokenizerFactory, tokenParsingMap);

        QueryParser queryParser = new QueryParser(new LinearEquationSolver(), infixToReversePolishTransformer);
        String applicationExitString = "exit";

        Application application = new Application(environment, queryParser, applicationExitString);
        application.run();
    }

    private static Map<Class<? extends Token>, InfixTransformer> createTokenParsingMap() {

        final Map<Class<? extends Token>, Integer> operatorToPrecedenceMap = createOperatorPrecedenceMap();
        final Map<Class<? extends Token>, InfixTransformer> tokenToOperationMap = new HashMap<>();

        InfixTransformer precedenceHandler = (token, tokenList, operatorStack) -> {
            tokenList.addAll(popAllWithLowerPrecedence(operatorToPrecedenceMap, operatorStack, operatorToPrecedenceMap.get(token.getClass())));
            operatorStack.push(token);
        };

        InfixTransformer pushOntoOperatorStack = (token, tokenList, operatorsStack) -> operatorsStack.push(token);

        tokenToOperationMap.put(NumberToken.class, (token, tokenList, operatorStack) -> tokenList.add(token));
        tokenToOperationMap.put(VariableToken.class, (token, tokenList, operatorStack) -> tokenList.add(token));

        tokenToOperationMap.put(MultiplicationOperatorToken.class, precedenceHandler);
        tokenToOperationMap.put(AdditionOperatorToken.class, precedenceHandler);
        tokenToOperationMap.put(SubtractionOperatorToken.class, precedenceHandler);
        tokenToOperationMap.put(DivisionOperatorToken.class, precedenceHandler);

        tokenToOperationMap.put(LogarithmOperatorToken.class, pushOntoOperatorStack);
        tokenToOperationMap.put(MinusUnaryOperatorToken.class, pushOntoOperatorStack);
        tokenToOperationMap.put(PlusUnaryOperatorToken.class, pushOntoOperatorStack);

        tokenToOperationMap.put(OpenParenthesisToken.class, pushOntoOperatorStack);
        tokenToOperationMap.put(CloseParenthesisToken.class, (token, tokenList, operatorStack) -> {
            tokenList.addAll(popAllUntilOpenParenthesisFound(operatorStack));
            operatorStack.pop();

            if(topOfStackIsUnaryFunction(operatorStack)) {
                tokenList.add(operatorStack.pop());
            }
        });

        return tokenToOperationMap;
    }

    private static List<Token> popAllUntilOpenParenthesisFound(Stack<Token> operators) {
        List<Token> tokens = new LinkedList<>();

        while (!operators.isEmpty() && operators.peek().getClass() != OpenParenthesisToken.class) {
            tokens.add(operators.pop());
        }

        if(operators.isEmpty()) {
            throw new QueryParseException("Mismatched parentheses");
        }

        return tokens;
    }

    private static List<Token> popAllWithLowerPrecedence(Map<Class<? extends Token>, Integer> operatorToPrecedenceMap,
                                                         Stack<Token> operators,
                                                         int precedence) {
        List<Token> tokens = new LinkedList<>();

        while (!operators.isEmpty()
                && operatorToPrecedenceMap.containsKey(operators.peek().getClass())
                && precedence <= operatorToPrecedenceMap.get(operators.peek().getClass())) {
            tokens.add(operators.pop());
        }

        return tokens;
    }

    private static boolean topOfStackIsUnaryFunction(Stack<Token> operatorStack) {
        return !operatorStack.isEmpty()
                && operatorStack.peek().getClass().getSuperclass().equals(UnaryOperatorToken.class);
    }

    private static Map<Class<? extends Token>, Integer> createOperatorPrecedenceMap() {
        final Map<Class<? extends Token>, Integer> operatorToPrecedenceMap = new HashMap<>();

        operatorToPrecedenceMap.put(MinusUnaryOperatorToken.class, 3);
        operatorToPrecedenceMap.put(DivisionOperatorToken.class, 2);
        operatorToPrecedenceMap.put(MultiplicationOperatorToken.class, 2);
        operatorToPrecedenceMap.put(AdditionOperatorToken.class, 1);
        operatorToPrecedenceMap.put(SubtractionOperatorToken.class, 1);

        return operatorToPrecedenceMap;
    }

    private static LinkedList<StringToTokenConverter> createTokenConverters() {
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
                ApplicationEntry::isImplicitMultiplicationSign,
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
