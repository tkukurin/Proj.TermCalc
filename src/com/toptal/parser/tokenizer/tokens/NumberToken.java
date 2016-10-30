
package com.toptal.parser.tokenizer.tokens;

import com.toptal.parser.LinearPolynomialNode;

import java.util.Stack;

public class NumberToken extends Token {
    public NumberToken(String representation) {
        super(false, representation);
    }

    @Override
    public void evaluate(Stack<LinearPolynomialNode> valueStack) {
        valueStack.push(new LinearPolynomialNode(Double.parseDouble(representation), null));
    }

    public static boolean isValid(char c) {
        return Character.isDigit(c) || c == '.';
    }
}