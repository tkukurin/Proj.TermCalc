package com.toptal.parser;

import java.util.Optional;

public class LinearPolynomialNodeOperation {

    public LinearPolynomialNode add(LinearPolynomialNode first, LinearPolynomialNode second) {
        Double resultFree = null, resultBound = null;

        if (eitherHasFreeValues(first, second)) {
            resultFree = valueOrZero(first.getFreeValue()) + valueOrZero(second.getFreeValue());
        }

        if (eitherHasBoundValues(first, second)) {
            resultBound = valueOrZero(first.getBoundValue()) + valueOrZero(second.getBoundValue());
        }

        return new LinearPolynomialNode(resultFree, resultBound);
    }

    public LinearPolynomialNode subtract(LinearPolynomialNode first, LinearPolynomialNode second) {
        return add(first, negate(second));
    }

    private LinearPolynomialNode negate(LinearPolynomialNode second) {
        return new LinearPolynomialNode(
                second.getFreeValue().map(val -> -val).orElse(null),
                second.getBoundValue().map(val -> -val).orElse(null));
    }

    public LinearPolynomialNode mutliply(LinearPolynomialNode first, LinearPolynomialNode second) {
        if(bothHaveBoundValues(first, second)) {
            throw new QueryParseException("Only linear equations are supported by this software");
        }

        Double resultFree = first.getFreeValue()
                .map(freeValue -> freeValue * second.getFreeValue().orElse(1.0))
                .orElse(second.getFreeValue().orElse(null));

        Double resultBound = null;

        if(first.getBoundValue().isPresent()) {
            resultBound = first.getBoundValue().get() * second.getFreeValue().orElse(1.0);
        }

        if(second.getBoundValue().isPresent()) {
            resultBound = second.getBoundValue().get() * first.getFreeValue().orElse(1.0);
        }

        return new LinearPolynomialNode(resultFree, resultBound);
    }

    private boolean bothHaveBoundValues(LinearPolynomialNode first, LinearPolynomialNode second) {
        return first.getBoundValue().isPresent() && second.getBoundValue().isPresent();
    }

    public LinearPolynomialNode divide(LinearPolynomialNode first, LinearPolynomialNode second) {
        if(eitherHasBoundValues(first, second)) {
            throw new QueryParseException("This software does not support division with linear equations");
        }

        // in case of divison by zero, let the exception propagate.
        double divisionResult = valueOrZero(first.getFreeValue()) / valueOrZero(second.getFreeValue());

        return new LinearPolynomialNode(divisionResult, null);
    }

    public LinearPolynomialNode logarithm(LinearPolynomialNode node) {
        if(node.getBoundValue().isPresent()) {
            throw new QueryParseException("Only linear equations are supported by this software");
        }

        if(!node.getFreeValue().isPresent()) {
            throw new QueryParseException("Logarithm operation requires an argument");
        }

        return new LinearPolynomialNode(Math.log(node.getFreeValue().get()), null);
    }

    private boolean eitherHasFreeValues(LinearPolynomialNode first, LinearPolynomialNode second) {
        return first.getFreeValue().isPresent() || second.getFreeValue().isPresent();
    }

    private boolean eitherHasBoundValues(LinearPolynomialNode first, LinearPolynomialNode second) {
        return first.getBoundValue().isPresent() || second.getBoundValue().isPresent();
    }

    private double valueOrZero(Optional<Double> value) {
        return value.orElse(0.0);
    }

}
