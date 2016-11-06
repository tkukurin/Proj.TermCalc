package com.toptal.parser;

import com.toptal.parser.exceptions.EquationSolveException;
import com.toptal.parser.exceptions.QueryParseException;
import com.toptal.parser.result.QueryParseNumericResult;
import com.toptal.parser.result.QueryParseResult;
import com.toptal.parser.result.QueryParseVariableResult;

import java.util.List;

public class LinearPolynomialNodeValueTransformer {

    private static final double EPSILON = 10e-9;

    QueryParseResult toQueryParseResult(List<LinearPolynomialNode> nodes) {
        return nodes.size() == 1
                ? getSimpleSolution(nodes.get(0))
                : solveEquation(nodes.get(0), nodes.get(1));
    }

    private QueryParseResult getSimpleSolution(LinearPolynomialNode linearPolynomialNode) {
        if (variableIsPresent(linearPolynomialNode)) {
            throw new QueryParseException("Linear equation should contain a right hand side");
        }

        return new QueryParseNumericResult(linearPolynomialNode.getFreeValue().get());
    }

    private boolean variableIsPresent(LinearPolynomialNode linearPolynomialNode) {
        return linearPolynomialNode.getBoundValue().isPresent();
    }

    private QueryParseResult solveEquation(LinearPolynomialNode leftHandSide,
                                           LinearPolynomialNode rightHandSide) {
        double xCoefficient = leftHandSide.getBoundValue().orElse(0.0) - rightHandSide.getBoundValue().orElse(0.0);
        double freeValue = -leftHandSide.getFreeValue().orElse(0.0) + rightHandSide.getFreeValue().orElse(0.0);

        if (zeroXEqualsNonZeroValue(xCoefficient, freeValue)) {
            throw new EquationSolveException("Equation does not have a solution");
        }

        return new QueryParseVariableResult(freeValue / xCoefficient);
    }

    private boolean zeroXEqualsNonZeroValue(double xCoefficient, double freeValue) {
        return doubleEquals(0, xCoefficient) && !doubleEquals(0, freeValue);
    }

    private boolean doubleEquals(double first, double second) {
        return Math.abs(first - second) < EPSILON;
    }

}
