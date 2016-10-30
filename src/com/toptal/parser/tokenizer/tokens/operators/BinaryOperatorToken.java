package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;
import com.toptal.parser.tokenizer.tokens.Token;

import java.util.Stack;
import java.util.function.BiFunction;

public abstract class BinaryOperatorToken extends Token {
    private final BiFunction<LinearPolynomialNode, LinearPolynomialNode, LinearPolynomialNode> evaluator;

    public BinaryOperatorToken(String representation,
                               BiFunction<LinearPolynomialNode, LinearPolynomialNode, LinearPolynomialNode> evaluator) {
        super(false, representation);
        this.evaluator = evaluator;
    }

    @Override
    public void evaluate(Stack<LinearPolynomialNode> valueStack) {
        LinearPolynomialNode top = valueStack.pop();
        LinearPolynomialNode bottom = valueStack.pop();

        valueStack.push(evaluator.apply(bottom, top));
    }
}
