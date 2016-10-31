package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;
import com.toptal.parser.tokenizer.tokens.Token;

import java.util.Stack;
import java.util.function.Function;

public abstract class UnaryOperatorToken extends Token {
    private final Function<LinearPolynomialNode, LinearPolynomialNode> evaluator;

    public UnaryOperatorToken(String representation,
                               Function<LinearPolynomialNode, LinearPolynomialNode> evaluator) {
        super(false, representation);
        this.evaluator = evaluator;
    }

    @Override
    public void evaluate(Stack<LinearPolynomialNode> valueStack) {
        LinearPolynomialNode top = valueStack.pop();
        valueStack.push(evaluator.apply(top));
    }
}
