package com.toptal.parser;

import com.toptal.parser.exceptions.QueryParseException;
import com.toptal.parser.result.QueryParseNumericResult;
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

    private final LinearEquationSolver equationSolver;
    private final InfixToReversePolishTransformer infixToReversePolishTransformer;

    public QueryParser(LinearEquationSolver equationSolver,
                       InfixToReversePolishTransformer infixToReversePolishTransformer) {
        this.equationSolver = equationSolver;
        this.infixToReversePolishTransformer = infixToReversePolishTransformer;
    }

    public QueryParseResult parse(String query) {
        String[] splitByEquals = query.replaceAll(SPACE, EMPTY_STRING).split(EQUALITY_SIGN);

        if (splitByEquals.length > 2) {
            throw new QueryParseException("Equation should contain only one equality sign");
        }

        List<LinearPolynomialNode> solutions = Arrays.stream(splitByEquals)
                .map(this::parseSingleQuery)
                .collect(Collectors.toList());

        return solutions.size() == 1
                ? getSimpleSolution(solutions.get(0))
                : equationSolver.solve(solutions.get(0), solutions.get(1));
    }

    private LinearPolynomialNode parseSingleQuery(String query) {
        Stack<LinearPolynomialNode> nodeStack = new Stack<>();

        try {
            infixToReversePolishTransformer.tokenizeToReversePolish(query).forEach(token -> token.evaluate(nodeStack));
        } catch(EmptyStackException e) {
            throw new QueryParseException("Missing operands in input string");
        }

        if (nodeStack.size() != 1) {
            throw new QueryParseException("Syntax error in input string");
        }

        return nodeStack.pop();
    }

    private QueryParseResult getSimpleSolution(LinearPolynomialNode linearPolynomialNode) {
        if (linearPolynomialNode.getBoundValue().isPresent()) {
            throw new QueryParseException("Linear equation should contain a right hand side");
        }

        return new QueryParseNumericResult(linearPolynomialNode.getFreeValue().get());
    }

}
