package com.toptal.parser;

import com.toptal.parser.exceptions.QueryParseException;
import com.toptal.parser.result.QueryParseResult;
import com.toptal.parser.transformer.InfixToReversePolishTransformer;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class QueryParser {

    private static final String EQUALITY_SIGN = "=";
    private static final String SPACE = " ";
    private static final String EMPTY_STRING = "";

    private final LinearPolynomialNodeValueTransformer valueTransformer;
    private final InfixToReversePolishTransformer infixToReversePolishTransformer;

    public QueryParser(LinearPolynomialNodeValueTransformer valueTransformer,
                       InfixToReversePolishTransformer infixToReversePolishTransformer) {
        this.valueTransformer = valueTransformer;
        this.infixToReversePolishTransformer = infixToReversePolishTransformer;
    }

    public QueryParseResult parse(String query) {
        String[] splitByEquals = requireAtMostOneEqualityCharacterSplit(query);

        List<LinearPolynomialNode> solutions = Arrays.stream(splitByEquals)
                .map(this::parseSingleQuery)
                .collect(Collectors.toList());

        return valueTransformer.toQueryParseResult(solutions);
    }

    private String[] requireAtMostOneEqualityCharacterSplit(String query) {
        String[] splitByEquals = query.replaceAll(SPACE, EMPTY_STRING).split(EQUALITY_SIGN);

        if (splitByEquals.length > 2) {
            throw new QueryParseException("Equation should contain only one equality sign");
        }

        return splitByEquals;
    }

    private LinearPolynomialNode parseSingleQuery(String query) {
        Stack<LinearPolynomialNode> tokenStack = new Stack<>();

        try {

            infixToReversePolishTransformer
                    .tokenizeToReversePolish(query)
                    .forEach(token -> token.evaluate(tokenStack));

        } catch(EmptyStackException e) {
            throw new QueryParseException("Missing operands in input string");
        }

        return requireExactlyOneElementOnStack(tokenStack);
    }

    private LinearPolynomialNode requireExactlyOneElementOnStack(Stack<LinearPolynomialNode> nodeStack) {
        if (nodeStack.size() != 1) {
            throw new QueryParseException("Syntax error in input string");
        }

        return nodeStack.pop();
    }

}
