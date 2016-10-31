package com.toptal.parser;

import com.toptal.parser.exception.QueryParseException;
import com.toptal.parser.tokenizer.Tokenizer;
import com.toptal.parser.tokenizer.TokenizerFactory;
import com.toptal.parser.tokenizer.tokens.Token;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class InfixToReversePolishTransformer {

    private final TokenizerFactory tokenizerFactory;
    private final Map<Class<? extends Token>, InfixTransformer> tokenToOperationMap;

    public InfixToReversePolishTransformer(TokenizerFactory tokenizerFactory,
                                           Map<Class<? extends Token>, InfixTransformer> tokenToOperationMap) {
        this.tokenizerFactory = tokenizerFactory;
        this.tokenToOperationMap = tokenToOperationMap;
    }

    List<Token> parse(String query) {
        Tokenizer tokenizer = tokenizerFactory.createTokenizer(query);

        List<Token> tokens = new LinkedList<>();
        Stack<Token> operators = new Stack<>();

        Token token = tokenizer.next();

        while(!token.isEndingState()) {
            InfixTransformer tokenHandler = tokenToOperationMap.get(token.getClass());

            if(tokenHandler == null) {
                throw new QueryParseException("Unrecognized token: " + token.toString());
            }

            tokenHandler.apply(token, tokens, operators);
            token = tokenizer.next();
        }

        while(!operators.isEmpty()) {
            tokens.add(operators.pop());
        }

        return tokens;
    }

}
