package com.toptal.parser.tokenizer.tokens;

import com.toptal.parser.LinearPolynomialNode;
import com.toptal.parser.exception.QueryParseException;

import java.util.Stack;

public abstract class Token {

    private final boolean isEndingState;
    private final String representation;

    public Token(boolean isEndingState, String representation) {
        this.isEndingState = isEndingState;
        this.representation = representation;
    }

    public boolean isEndingState() {
        return isEndingState;
    }

    public String getRepresentation() { return this.representation; }

    public void evaluate(Stack<LinearPolynomialNode> valueStack) {
        throw new QueryParseException("Encountered unexpected token evaluating input: '" + this.representation + "'");
    }

}