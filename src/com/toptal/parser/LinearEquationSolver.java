package com.toptal.parser;

import com.toptal.parser.result.QueryParseResult;
import com.toptal.parser.result.QueryParseVariableResult;

public class LinearEquationSolver {

    QueryParseResult solve(LinearPolynomialNode lhs, LinearPolynomialNode rhs) {
        LinearPolynomialNode x = new LinearPolynomialNode(
                null, lhs.getBoundValue().orElse(0.0) - rhs.getBoundValue().orElse(0.0));

        LinearPolynomialNode free = new LinearPolynomialNode(
                -lhs.getFreeValue().orElse(0.0) + rhs.getFreeValue().orElse(0.0), null);

        return new QueryParseVariableResult(free.getFreeValue().get() / x.getBoundValue().get());
    }

}
