package com.toptal.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

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

    public static LinearPolynomialNode parse(String query) {
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
