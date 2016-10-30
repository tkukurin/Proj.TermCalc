package com.toptal.parser;

import com.toptal.parser.result.QueryParseNumericResult;
import com.toptal.parser.result.QueryParseResult;
import com.toptal.parser.result.QueryParseVariableResult;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QueryParser {

    private static final Map<String, BiFunction<LinearPolynomialNode, LinearPolynomialNode, LinearPolynomialNode>>
            binaryOperationsMap = new HashMap<>();
    private static final Map<String, Function<LinearPolynomialNode, LinearPolynomialNode>>
            unaryOperationsMap = new HashMap<>();
    private static final LinearPolynomialNodeOperation operationExecutor = new LinearPolynomialNodeOperation();

    static {
        binaryOperationsMap.put("+", operationExecutor::add);
        binaryOperationsMap.put("-", operationExecutor::subtract);
        binaryOperationsMap.put("/", operationExecutor::divide);
        binaryOperationsMap.put("*", operationExecutor::mutliply);

        unaryOperationsMap.put("log", operationExecutor::logarithm);
    }

    public static QueryParseResult parse(String query) {
        String[] splitByEquals = query.split("=");

        if(splitByEquals.length > 2) {
            throw new QueryParseException("Too many equality signs encountered");
        }

        List<LinearPolynomialNode> solutions = Arrays.stream(splitByEquals)
                .map(QueryParser::_parse)
                .collect(Collectors.toList());

        if(solutions.size() == 1) {
            return new QueryParseNumericResult(solutions.get(0).getFreeValue().get());
        }

        LinearPolynomialNode lhs = solutions.get(0);
        LinearPolynomialNode rhs = solutions.get(1);

        LinearPolynomialNode x = new LinearPolynomialNode(
                null, lhs.getBoundValue().orElse(0.0) - rhs.getBoundValue().orElse(0.0));
        LinearPolynomialNode free = new LinearPolynomialNode(
                -lhs.getFreeValue().orElse(0.0) + rhs.getFreeValue().orElse(0.0), null);

        return new QueryParseVariableResult(free.getFreeValue().get() / x.getBoundValue().get());
    }

    public static LinearPolynomialNode _parse(String query) {
        Stack<LinearPolynomialNode> nodes = new Stack<>();

        InfixToReversePolishTransformer.parse(query).forEach(token -> {
            Optional<Double> tokenAsDouble = tryParseAsDouble(token);

            if(tokenAsDouble.isPresent()) {
                nodes.push(new LinearPolynomialNode(tokenAsDouble.get(), null));
            } else if("x".equals(token)) {
                nodes.push(new LinearPolynomialNode(null, 1.0));
            } else if(binaryOperationsMap.containsKey(token)) {
                LinearPolynomialNode above = nodes.pop();
                LinearPolynomialNode below = nodes.pop();
                LinearPolynomialNode result = binaryOperationsMap.get(token).apply(below, above);
                nodes.push(result);
            } else if(unaryOperationsMap.containsKey(token)) {
                LinearPolynomialNode result = unaryOperationsMap.get(token).apply(nodes.pop());
                nodes.push(result);
            } else {
                throw new QueryParseException("Unexpected token: " + token);
            }
        });

        if(nodes.size() != 1) {
            throw new QueryParseException("Syntax error in input string");
        }

        return nodes.pop();
    }

    private static Optional<Double> tryParseAsDouble(String token) {
        try {
            return Optional.of(Double.parseDouble(token));
        } catch (Exception ignore) {}

        return Optional.empty();
    }

}
