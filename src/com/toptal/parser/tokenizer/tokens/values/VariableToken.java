package com.toptal.parser.tokenizer.tokens.values;

import com.toptal.parser.LinearPolynomialNode;
import com.toptal.parser.tokenizer.tokens.Token;

import java.util.Stack;

public class VariableToken extends Token {

    public static final char REPRESENTATION = 'x';

    public VariableToken() {
        super(false, Character.toString(REPRESENTATION));
    }

    @Override
    public void evaluate(Stack<LinearPolynomialNode> valueStack) {
        valueStack.push(new LinearPolynomialNode(null, 1.0));
    }
}
