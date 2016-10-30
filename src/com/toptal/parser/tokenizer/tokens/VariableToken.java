package com.toptal.parser.tokenizer.tokens;

import com.toptal.parser.LinearPolynomialNode;

import java.util.Stack;

public class VariableToken extends Token {

    public VariableToken(String representation) {
        super(false, representation);
    }

    @Override
    public void evaluate(Stack<LinearPolynomialNode> valueStack) {
        valueStack.push(new LinearPolynomialNode(null, 1.0));
    }
}
