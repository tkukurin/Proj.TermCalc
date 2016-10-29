package com.toptal.parser;

import com.toptal.parser.result.QueryParseResult;

public class QueryParser {

    private char[] input;
    private int position;

    private QueryParser(String input) {
        this.position = 0;
        this.input = input.toCharArray();
    }

    private QueryParseResult parse() {
        while(Character.isWhitespace(input[position])) {
            position++;
        }

        // or char starts with "." ?
        if(Character.isDigit(input[position])) {
            // push number.
            parseNumeric();
        }

        if(input[position] == '(') {

        }

        return null;
    }

    private void parseNumeric() {

    }

    public static QueryParseResult parse(String input) {
        return new QueryParser(input).parse();
    }

}
