package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;
import com.toptal.parser.LinearPolynomialNodeOperation;

public class DivisionOperatorToken extends BinaryOperatorToken {
    public DivisionOperatorToken() {
        super("/", LinearPolynomialNode::divide);
    }
}
