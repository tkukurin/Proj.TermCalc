package com.toptal.parser;

import com.toptal.parser.tokenizer.tokens.Token;

import java.util.List;
import java.util.Stack;

@FunctionalInterface
public interface InfixToReversePolishTokenTransformer {
    void apply(Token current, List<Token> outputList, Stack<Token> operators);
}
