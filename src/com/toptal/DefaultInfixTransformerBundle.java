package com.toptal;

import com.toptal.parser.exceptions.QueryParseException;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.operators.*;
import com.toptal.parser.tokenizer.tokens.state.CloseParenthesisToken;
import com.toptal.parser.tokenizer.tokens.state.OpenParenthesisToken;
import com.toptal.parser.tokenizer.tokens.values.NumberToken;
import com.toptal.parser.tokenizer.tokens.values.VariableToken;
import com.toptal.parser.transformer.InfixToReversePolishTokenTransformMap;
import com.toptal.parser.transformer.InfixToReversePolishTokenTransformer;

import java.util.*;

public class DefaultInfixTransformerBundle {

    public static InfixToReversePolishTokenTransformMap createTokenParsingMap() {
        final InfixToReversePolishTokenTransformMap tokenToOperationMap = new InfixToReversePolishTokenTransformMap();

        final InfixToReversePolishTokenTransformer precedenceHandler = createPrecedenceHandler();
        final InfixToReversePolishTokenTransformer closeParenthesisHandler = createCloseParenthesisHandler();
        final InfixToReversePolishTokenTransformer pushOntoOperatorStack = (token, tokenList, operatorsStack) -> operatorsStack.push(token);
        final InfixToReversePolishTokenTransformer addToTokenList = (token, tokenList, operatorStack) -> tokenList.add(token);

        tokenToOperationMap.put(NumberToken.class, addToTokenList);
        tokenToOperationMap.put(VariableToken.class, addToTokenList);

        tokenToOperationMap.put(MultiplicationBinaryOperatorToken.class, precedenceHandler);
        tokenToOperationMap.put(AdditionBinaryOperatorToken.class, precedenceHandler);
        tokenToOperationMap.put(SubtractionBinaryOperatorToken.class, precedenceHandler);
        tokenToOperationMap.put(DivisionBinaryOperatorToken.class, precedenceHandler);

        tokenToOperationMap.put(LogarithmFunctionToken.class, pushOntoOperatorStack);
        tokenToOperationMap.put(MinusUnaryOperatorToken.class, pushOntoOperatorStack);
        tokenToOperationMap.put(PlusUnaryOperatorToken.class, pushOntoOperatorStack);

        tokenToOperationMap.put(OpenParenthesisToken.class, pushOntoOperatorStack);
        tokenToOperationMap.put(CloseParenthesisToken.class, closeParenthesisHandler);

        return tokenToOperationMap;
    }

    private static InfixToReversePolishTokenTransformer createPrecedenceHandler() {
        final Map<Class<? extends Token>, Integer> operatorToPrecedenceMap = createOperatorPrecedenceMap();
        return (token, tokenList, operatorStack) -> {
            int currentTokenPrecedence = operatorToPrecedenceMap.get(token.getClass());
            tokenList.addAll(popAllWithHigherPrecedence(operatorToPrecedenceMap, operatorStack, currentTokenPrecedence));
            operatorStack.push(token);
        };
    }

    private static Map<Class<? extends Token>, Integer> createOperatorPrecedenceMap() {
        final Map<Class<? extends Token>, Integer> operatorToPrecedenceMap = new HashMap<>();

        operatorToPrecedenceMap.put(MinusUnaryOperatorToken.class, 3);
        operatorToPrecedenceMap.put(PlusUnaryOperatorToken.class, 3);
        operatorToPrecedenceMap.put(DivisionBinaryOperatorToken.class, 2);
        operatorToPrecedenceMap.put(MultiplicationBinaryOperatorToken.class, 2);
        operatorToPrecedenceMap.put(AdditionBinaryOperatorToken.class, 1);
        operatorToPrecedenceMap.put(SubtractionBinaryOperatorToken.class, 1);

        return operatorToPrecedenceMap;
    }

    private static List<Token> popAllWithHigherPrecedence(Map<Class<? extends Token>, Integer> operatorToPrecedenceMap,
                                                          Stack<Token> operators,
                                                          int currentOperatorPrecedence) {
        List<Token> tokensFromTop = new LinkedList<>();

        while (!operators.isEmpty()
                && operatorToPrecedenceMap.containsKey(operators.peek().getClass())
                && currentOperatorPrecedence <= operatorToPrecedenceMap.get(operators.peek().getClass())) {
            tokensFromTop.add(operators.pop());
        }

        return tokensFromTop;
    }

    private static InfixToReversePolishTokenTransformer createCloseParenthesisHandler() {
        return (token, tokenList, operatorStack) -> {
            tokenList.addAll(popAllUntilOpenParenthesisFound(operatorStack));
            operatorStack.pop();

            if(topOfStackIsUnaryFunction(operatorStack)) {
                tokenList.add(operatorStack.pop());
            }
        };
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

    private static boolean topOfStackIsUnaryFunction(Stack<Token> operatorStack) {
        return !operatorStack.isEmpty()
                && operatorStack.peek() instanceof UnaryOperatorToken;
    }

}
