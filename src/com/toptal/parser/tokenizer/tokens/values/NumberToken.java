
package com.toptal.parser.tokenizer.tokens.values;

import com.toptal.parser.LinearPolynomialNode;
import com.toptal.parser.exceptions.QueryParseException;
import com.toptal.parser.tokenizer.tokens.Token;

import java.util.Stack;

public class NumberToken extends Token {
    public NumberToken(String representation) {
        super(false, representation);
    }

    @Override
    public void evaluate(Stack<LinearPolynomialNode> valueStack) {
        try {
            valueStack.push(new LinearPolynomialNode(Double.parseDouble(toString()), null));
        } catch(NumberFormatException e) {
            throw new QueryParseException("Invalid double value: " + toString());
        }
    }

}