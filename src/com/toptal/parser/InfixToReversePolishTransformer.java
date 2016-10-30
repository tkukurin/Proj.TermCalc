package com.toptal.parser;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;

public class InfixToReversePolishTransformer {

    private static final Map<String, Integer> operatorToPrecedenceMap = new HashMap<>();
    static {
        operatorToPrecedenceMap.put("/", 2);
        operatorToPrecedenceMap.put("*", 2);
        operatorToPrecedenceMap.put("+", 1);
        operatorToPrecedenceMap.put("-", 1);
    }

    public static List<String> parse(String query) {
        List<String> tokens = new LinkedList<>();
        Stack<String> operators = new Stack<>();

        for(int i = 0; i < query.length(); i++) {
            char c = query.charAt(i);
            String s = Character.toString(c);

            if (Character.isWhitespace(c)) {
                continue;
            }

            if (canBeParsedAsDouble(c)) {
                int endIndex = findEndingIndexForDoubleChar(query, i + 1);
                tokens.add(query.substring(i, endIndex));
                i = endIndex - 1;
            } else if(operatorToPrecedenceMap.containsKey(s)) {
                transferAllWithLowerPrecedenceToTokenList(tokens, operators, operatorToPrecedenceMap.get(s));
                operators.push(s);
            } else if(c == '(') {
                operators.push(s);
            } else if(c == ')') {
                transferAllToTokenListUntilOpenParenthesisFound(tokens, operators);
            } else if(isVariable(c)) {
                tokens.add(s);
            } else {
                throw new QueryParseException("Unrecognized token: " + c);
            }
        }

        while(!operators.isEmpty()) {
            tokens.add(operators.pop());
        }

        return tokens;
    }

    private static void transferAllToTokenListUntilOpenParenthesisFound(List<String> tokens, Stack<String> operators) {
        while (!"(".equals(operators.peek())) {
            tokens.add(operators.pop());
        }

        operators.pop();
    }

    private static void transferAllWithLowerPrecedenceToTokenList(List<String> tokens, Stack<String> operators, int precedence) {
        while (!operators.isEmpty()
                && operatorToPrecedenceMap.containsKey(operators.peek())
                && precedence <= operatorToPrecedenceMap.get(operators.peek())) {
            tokens.add(operators.pop());
        }
    }

    private static boolean isVariable(char c) {
        return c == 'x';
    }

    private static int findEndingIndexForDoubleChar(String query, int startAt) {
        while(startAt < query.length() && canBeParsedAsDouble(query.charAt(startAt)) ) {
            startAt++;
        }

        return startAt;
    }

    private static boolean canBeParsedAsDouble(char c) {
        return Character.isDigit(c) || c == '.';
    }

}
