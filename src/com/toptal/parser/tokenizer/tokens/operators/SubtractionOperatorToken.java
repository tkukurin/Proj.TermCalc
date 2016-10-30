package com.toptal.parser.tokenizer.tokens.operators;

import com.toptal.parser.LinearPolynomialNode;
import com.toptal.parser.LinearPolynomialNodeOperation;

public class SubtractionOperatorToken extends BinaryOperatorToken {

    public SubtractionOperatorToken() {
        super("-", LinearPolynomialNode::subtract);
    }
}
