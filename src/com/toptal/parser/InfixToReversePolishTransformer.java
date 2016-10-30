package com.toptal.parser;

import com.toptal.parser.tokenizer.*;
import com.toptal.parser.tokenizer.tokens.*;
import com.toptal.parser.tokenizer.tokens.operators.*;

import java.util.*;

public class InfixToReversePolishTransformer {
    private static final Map<Class<? extends Token>, Integer> operatorToPrecedenceMap = new HashMap<>();
    static {
        operatorToPrecedenceMap.put(DivisionOperatorToken.class, 2);
        operatorToPrecedenceMap.put(MultiplicationOperatorToken.class, 2);
        operatorToPrecedenceMap.put(AddOperatorToken.class, 1);
        operatorToPrecedenceMap.put(SubtractionOperatorToken.class, 1);
    }

    @FunctionalInterface
    interface TriConsumer {
        void apply(Token current, List<Token> outputList, Stack<Token> operators);
    }

    private static final Map<Class<? extends Token>, TriConsumer> tokenToOperationMap = new HashMap<>();
    static {
        TriConsumer binaryOperationHandler = (token, tokenList, operatorStack) -> {
            tokenList.addAll(popOffAllWithLowerPrecedence(operatorStack, operatorToPrecedenceMap.get(token.getClass())));
            operatorStack.push(token);
        };

        tokenToOperationMap.put(NumberToken.class, (token, tokenList, operatorStack) -> tokenList.add(token));
        tokenToOperationMap.put(MultiplicationOperatorToken.class, binaryOperationHandler);
        tokenToOperationMap.put(AddOperatorToken.class, binaryOperationHandler);
        tokenToOperationMap.put(SubtractionOperatorToken.class, binaryOperationHandler);
        tokenToOperationMap.put(DivisionOperatorToken.class, binaryOperationHandler);
        tokenToOperationMap.put(OpenParenthesisToken.class, (token, tokenList, operatorStack) -> operatorStack.push(token));
        tokenToOperationMap.put(CloseParenthesisToken.class, (token, tokenList, operatorStack) -> {
            tokenList.addAll(popOffAllUntilOpenParenthesisFound(operatorStack));
            operatorStack.pop();
        });
        tokenToOperationMap.put(VariableToken.class, (token, tokenList, operatorStack) -> tokenList.add(token));
        tokenToOperationMap.put(UnaryOperatorToken.class, (token, tokenList, operatorStack) -> operatorStack.push(token));

    }

    public static List<Token> parse(String query) {
        Tokenizer tokenizer = new Tokenizer(query);
        List<Token> tokens = new LinkedList<>();
        Stack<Token> operators = new Stack<>();
        Token token = tokenizer.next();

        while(!token.isEndingState()) {
            TriConsumer tokenHandler = tokenToOperationMap.get(token.getClass());

            if(tokenHandler == null) {
                throw new QueryParseException("Unrecognized token: " + token.getRepresentation());
            }

            tokenHandler.apply(token, tokens, operators);
            token = tokenizer.next();
        }

        while(!operators.isEmpty()) {
            tokens.add(operators.pop());
        }

        return tokens;
    }

    private static List<Token> popOffAllUntilOpenParenthesisFound(Stack<Token> operators) {
        List<Token> tokens = new LinkedList<>();

        while (!operators.isEmpty() && operators.peek().getClass() != OpenParenthesisToken.class) {
            tokens.add(operators.pop());
        }

        if(operators.isEmpty()) {
            throw new QueryParseException("Mismatched parentheses");
        }

        return tokens;
    }

    private static List<Token> popOffAllWithLowerPrecedence(Stack<Token> operators, int precedence) {
        List<Token> tokens = new LinkedList<>();

        while (!operators.isEmpty()
                && operatorToPrecedenceMap.containsKey(operators.peek().getClass())
                && precedence <= operatorToPrecedenceMap.get(operators.peek().getClass())) {
            tokens.add(operators.pop());
        }

        return tokens;
    }

}
