package com.toptal.parser;

import com.toptal.parser.exception.QueryParseException;
import com.toptal.parser.result.QueryParseNumericResult;
import com.toptal.parser.result.QueryParseResult;

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

    private QueryParseResult getSimpleSolution(LinearPolynomialNode linearPolynomialNode) {
        if (linearPolynomialNode.getBoundValue().isPresent()) {
            throw new QueryParseException("Linear equation should contain a right hand side");
        }

        return new QueryParseNumericResult(linearPolynomialNode.getFreeValue().get());
    }

    private LinearPolynomialNode parseSingleQuery(String query) {
        Stack<LinearPolynomialNode> nodes = new Stack<>();

        try {
            infixToReversePolishTransformer.parse(query).forEach(token -> token.evaluate(nodes));
        } catch(EmptyStackException e) {
            throw new QueryParseException("Missing operands in input string");
        }

        if (nodes.size() != 1) {
            throw new QueryParseException("Syntax error in input string");
        }

        return nodes.pop();
    }

}
