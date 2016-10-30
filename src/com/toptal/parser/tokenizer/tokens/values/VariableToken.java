package com.toptal.parser.tokenizer.tokens.values;

import com.toptal.parser.LinearPolynomialNode;
import com.toptal.parser.tokenizer.tokens.Token;

import java.util.Stack;

public class VariableToken extends Token {

    public VariableToken() {
        super(false, "x");
    }

    @Override
    public void evaluate(Stack<LinearPolynomialNode> valueStack) {
        valueStack.push(new LinearPolynomialNode(null, 1.0));
    }
}
