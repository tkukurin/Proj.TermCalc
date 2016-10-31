package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;

import java.util.function.Function;

public abstract class FunctionToken extends UnaryOperatorToken {
    public FunctionToken(String representation, Function<LinearPolynomialNode, LinearPolynomialNode> evaluator) {
        super(representation, evaluator);
    }
}
