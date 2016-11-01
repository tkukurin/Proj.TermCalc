package com.toptal.parser.transformer;

import com.toptal.parser.exceptions.QueryParseException;
import com.toptal.parser.tokenizer.Tokenizer;
import com.toptal.parser.tokenizer.TokenizerFactory;
import com.toptal.parser.tokenizer.tokens.Token;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class InfixToReversePolishTransformer {

    private final TokenizerFactory tokenizerFactory;
    private final InfixToReversePolishTokenTransformMap tokenToOperationMap;

    public InfixToReversePolishTransformer(TokenizerFactory tokenizerFactory,
                                           InfixToReversePolishTokenTransformMap tokenToOperationMap) {
        this.tokenizerFactory = tokenizerFactory;
        this.tokenToOperationMap = tokenToOperationMap;
    }

    public List<Token> tokenizeToReversePolish(String query) {
        Tokenizer tokenizer = tokenizerFactory.createTokenizer(query);

        List<Token> tokens = new LinkedList<>();
        Stack<Token> operators = new Stack<>();

        Token token = tokenizer.next();

        while(!token.isEndingState()) {
            InfixToReversePolishTokenTransformer tokenHandler = tokenToOperationMap.get(token.getClass());

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
