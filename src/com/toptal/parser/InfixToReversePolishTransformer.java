package com.toptal.parser;

import com.toptal.parser.tokenizer.Tokenizer;

import java.util.*;

public class InfixToReversePolishTransformer {

    public static final Map<String, Integer> operatorToPrecedenceMap = new HashMap<>();
    static {
        operatorToPrecedenceMap.put("/", 2);
        operatorToPrecedenceMap.put("*", 2);
        operatorToPrecedenceMap.put("+", 1);
        operatorToPrecedenceMap.put("-", 1);
    }

    public static List<String> parse(String query) {
        Tokenizer tokenizer = new Tokenizer(query);

        List<String> tokens = new LinkedList<>();
        Stack<String> operators = new Stack<>();
        String nextToken = tokenizer.next();

        while(!"EOF".equals(nextToken)) {
            char c = nextToken.charAt(0);

            if (canBeParsedAsDouble(c)) {
                tokens.add(nextToken);
            } else if(operatorToPrecedenceMap.containsKey(nextToken)) {
                tokens.addAll(popOffAllWithLowerPrecedence(operators, operatorToPrecedenceMap.get(nextToken)));
                operators.push(nextToken);
            } else if(c == '(') {
                operators.push(nextToken);
            } else if(c == ')') {
                tokens.addAll(popOffAllUntilOpenParenthesisFound(operators));
                operators.pop();
            } else if(isVariable(c)) {
                tokens.add(nextToken);
            } else if(c == 'l') {
                operators.push(nextToken);
            } else {
                throw new QueryParseException("Unrecognized token: " + c);
            }

            nextToken = tokenizer.next();
        }

        while(!operators.isEmpty()) {
            tokens.add(operators.pop());
        }

        return tokens;
    }

    private static List<String> popOffAllUntilOpenParenthesisFound(Stack<String> operators) {
        List<String> tokens = new LinkedList<>();

        while (!operators.isEmpty() && !"(".equals(operators.peek())) {
            tokens.add(operators.pop());
        }

        if(operators.isEmpty()) {
            throw new QueryParseException("Mismatched parentheses");
        }

        return tokens;
    }

    private static List<String> popOffAllWithLowerPrecedence(Stack<String> operators, int precedence) {
        List<String> tokens = new LinkedList<>();

        while (!operators.isEmpty()
                && operatorToPrecedenceMap.containsKey(operators.peek())
                && precedence <= operatorToPrecedenceMap.get(operators.peek())) {
            tokens.add(operators.pop());
        }

        return tokens;
    }

    private static boolean isVariable(char c) {
        return c == 'x';
    }

    private static boolean canBeParsedAsDouble(char c) {
        return Character.isDigit(c) || c == '.';
    }

}
