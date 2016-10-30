package com.toptal;

import com.toptal.parser.InfixTransformer;
import com.toptal.parser.exception.QueryParseException;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.operators.*;
import com.toptal.parser.tokenizer.tokens.state.CloseParenthesisToken;
import com.toptal.parser.tokenizer.tokens.state.OpenParenthesisToken;
import com.toptal.parser.tokenizer.tokens.values.NumberToken;
import com.toptal.parser.tokenizer.tokens.values.VariableToken;

import java.util.*;

public class DefaultInfixTransformerBundle {

    public static Map<Class<? extends Token>, InfixTransformer> createTokenParsingMap() {

        final Map<Class<? extends Token>, Integer> operatorToPrecedenceMap = createOperatorPrecedenceMap();
        final Map<Class<? extends Token>, InfixTransformer> tokenToOperationMap = new HashMap<>();

        InfixTransformer precedenceHandler = (token, tokenList, operatorStack) -> {
            tokenList.addAll(popAllWithLowerPrecedence(operatorToPrecedenceMap, operatorStack, operatorToPrecedenceMap.get(token.getClass())));
            operatorStack.push(token);
        };

        InfixTransformer pushOntoOperatorStack = (token, tokenList, operatorsStack) -> operatorsStack.push(token);
        InfixTransformer addToTokenList = (token, tokenList, operatorStack) -> tokenList.add(token);

        tokenToOperationMap.put(NumberToken.class, addToTokenList);
        tokenToOperationMap.put(VariableToken.class, addToTokenList);

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

}