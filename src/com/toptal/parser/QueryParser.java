package com.toptal.parser;

import com.toptal.parser.result.QueryParseNumericResult;
import com.toptal.parser.result.QueryParseResult;
import com.toptal.parser.result.QueryParseVariableResult;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class QueryParser {

    public static QueryParseResult parse(String query) {
        String[] splitByEquals = query.split("=");

        if(splitByEquals.length > 2) {
            throw new QueryParseException("Equation should contain only one equality sign");
        }

        List<LinearPolynomialNode> solutions = Arrays.stream(splitByEquals)
                .map(QueryParser::_parse)
                .collect(Collectors.toList());

        return solutions.size() == 1
                ? new QueryParseNumericResult(solutions.get(0).getFreeValue().get())
                : solveEquation(solutions.get(0), solutions.get(1));
    }

    private static QueryParseResult solveEquation(LinearPolynomialNode lhs, LinearPolynomialNode rhs) {
        LinearPolynomialNode x = new LinearPolynomialNode(
                null, lhs.getBoundValue().orElse(0.0) - rhs.getBoundValue().orElse(0.0));
        LinearPolynomialNode free = new LinearPolynomialNode(
                -lhs.getFreeValue().orElse(0.0) + rhs.getFreeValue().orElse(0.0), null);

        return new QueryParseVariableResult(free.getFreeValue().get() / x.getBoundValue().get());
    }

    public static LinearPolynomialNode _parse(String query) {
        Stack<LinearPolynomialNode> nodes = new Stack<>();

        InfixToReversePolishTransformer.parse(query)
                .forEach(token -> token.evaluate(nodes));

        if(nodes.size() != 1) {
            throw new QueryParseException("Syntax error in input string");
        }

        return nodes.pop();
    }

}
