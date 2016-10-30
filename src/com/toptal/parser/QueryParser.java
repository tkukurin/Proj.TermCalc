package com.toptal.parser;

import com.toptal.parser.result.QueryParseResult;

import java.util.Optional;
import java.util.Stack;

public class QueryParser {

    public static QueryParseResult parse(String query) {
        Stack<LinearPolynomialNode> nodes = new Stack<>();

        InfixToReversePolishTransformer.parse(query).forEach(token -> {
            Optional<Double> tokenAsDouble = tryParseAsDouble(token);

            if(tokenAsDouble.isPresent()) {
                nodes.push(new LinearPolynomialNode(tokenAsDouble.get(), null));
            } else if("x".equals(token)) {
                nodes.push(new LinearPolynomialNode(null, 1.0));
            } else {
                // nodes.push(getOperation(token).execute(nodes));
            }
        });

        return null;
    }

    private static Optional<Double> tryParseAsDouble(String token) {
        try {
            return Optional.of(Double.parseDouble(token));
        } catch (Exception ignore) {}

        return Optional.empty();
    }

}
