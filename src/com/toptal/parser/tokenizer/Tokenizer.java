package com.toptal.parser.tokenizer;

import com.toptal.parser.InfixToReversePolishTransformer;
import com.toptal.parser.QueryParseException;

public class Tokenizer {

    private String state;
    private int position;

    private final String input;

    public Tokenizer(String input) {
        this.input = input;
        this.position = 0;
    }

    public String next() {

        while (position < input.length() && Character.isWhitespace(currentCharacter())) {
            position++;
        }

        if(position == input.length()) {
            return "EOF";
        }

        if (canBeParsedAsDouble(currentCharacter())) {
            final int startingPosition = position;
            final int endingPosition = advancePositionToNextNonDoubleToken();

            state = "NUMBER";

            return input.substring(startingPosition, endingPosition);
        }

        if(InfixToReversePolishTransformer.operatorToPrecedenceMap.containsKey(currentCharacter() + "")) {
            String operator = currentCharacter() + "";
            state = "OPERATOR";

            position++;
            return operator;
        }

        if (currentCharacter() == '(') {
            if("NUMBER".equals(state) || "CLOSE_PAREN".equals(state) || "VARIABLE".equals(state)) {
                state = "OPERATOR";
                return "*";
            }

            state = "OPEN_PAREN";
            position++;
            return "(";
        }

        if(currentCharacter() == ')') {
            position++;
            state = "CLOSE_PAREN";
            return ")";
        }

        if(currentCharacter() == 'x') {
            if("NUMBER".equals(state)) {
                state = "OPERATOR";
                return "*";
            }

            state = "VARIABLE";
            position++;
            return "x";
        }

        if(currentCharacter() == 'l') {
            // TODO parse log
            position += 3;
            state = "LOG";
            return "log";
        }

        throw new QueryParseException("Error near character: "  + currentCharacter());
    }

    private int advancePositionToNextNonDoubleToken() {
        while(position < input.length() && canBeParsedAsDouble(currentCharacter())) {
            position++;
        }

        return position;
    }

    private boolean canBeParsedAsDouble(char c) {
        return Character.isDigit(c) || c == '.';
    }

    private char currentCharacter() {
        return input.charAt(position);
    }

}
