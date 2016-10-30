
package com.toptal.parser.tokenizer.tokens;

import com.toptal.parser.LinearPolynomialNode;
import com.toptal.parser.QueryParseException;

import java.util.Stack;

public class NumberToken extends Token {
    public NumberToken(String representation) {
        super(false, representation);
    }

    @Override
    public void evaluate(Stack<LinearPolynomialNode> valueStack) {
        try {
            valueStack.push(new LinearPolynomialNode(Double.parseDouble(representation), null));
        } catch(NumberFormatException e) {
            throw new QueryParseException("Invalid double value: " + representation);
        }
    }

}