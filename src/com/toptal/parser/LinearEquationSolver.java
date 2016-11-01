package com.toptal.parser;

import com.toptal.parser.exceptions.EquationSolveException;
import com.toptal.parser.result.QueryParseResult;
import com.toptal.parser.result.QueryParseVariableResult;

public class LinearEquationSolver {

    private static final double EPSILON = 10e-9;

    QueryParseResult solve(LinearPolynomialNode leftHandSide,
                           LinearPolynomialNode rightHandSide) {
        double xCoefficient = leftHandSide.getBoundValue().orElse(0.0) - rightHandSide.getBoundValue().orElse(0.0);
        double freeValue = -leftHandSide.getFreeValue().orElse(0.0) + rightHandSide.getFreeValue().orElse(0.0);

        if(doubleEquals(0, xCoefficient) && !doubleEquals(0, freeValue)) {
            throw new EquationSolveException("Equation does not have a solution");
        }

        return new QueryParseVariableResult(freeValue / xCoefficient);
    }

    private boolean doubleEquals(double first, double second) {
        return Math.abs(first - second) < EPSILON;
    }

}
