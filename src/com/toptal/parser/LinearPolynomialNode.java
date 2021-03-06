package com.toptal.parser;

import com.toptal.parser.exceptions.PolynomialOperationException;

import java.util.Optional;

public class LinearPolynomialNode {

    private final Double freeValue;
    private final Double xCoefficient;

    public LinearPolynomialNode(Double freeValue,
                                Double xCoefficient) {
        this.freeValue = freeValue;
        this.xCoefficient = xCoefficient;
    }

    Optional<Double> getFreeValue() {
        return Optional.ofNullable(freeValue);
    }

    Optional<Double> getBoundValue() {
        return Optional.ofNullable(xCoefficient);
    }

    public LinearPolynomialNode add(LinearPolynomialNode second) {
        Double resultFree = null, resultBound = null;

        if (eitherHasFreeValues(this, second)) {
            resultFree = valueOrZero(this.getFreeValue()) + valueOrZero(second.getFreeValue());
        }

        if (eitherHasBoundValues(this, second)) {
            resultBound = valueOrZero(this.getBoundValue()) + valueOrZero(second.getBoundValue());
        }

        return new LinearPolynomialNode(resultFree, resultBound);
    }

    public LinearPolynomialNode subtract(LinearPolynomialNode second) {
        return add(second.negate());
    }

    public LinearPolynomialNode negate() {
        return new LinearPolynomialNode(
                this.getFreeValue().map(val -> -val).orElse(null),
                this.getBoundValue().map(val -> -val).orElse(null));
    }

    public LinearPolynomialNode mutliply(LinearPolynomialNode second) {
        if(bothHaveBoundValues(this, second)) {
            throw new PolynomialOperationException("Only linear equations are supported by this software");
        }

        Double resultFree = null;
        if(this.getFreeValue().isPresent() && second.getFreeValue().isPresent()) {
            resultFree = this.getFreeValue().get() * second.getFreeValue().get();
        }

        Double resultBound = null;
        if(this.getBoundValue().isPresent()) {
            resultBound = this.getBoundValue().get() * second.getFreeValue().orElse(1.0);
        } else if(second.getBoundValue().isPresent()) {
            resultBound = second.getBoundValue().get() * this.getFreeValue().orElse(1.0);
        }

        return new LinearPolynomialNode(resultFree, resultBound);
    }

    public LinearPolynomialNode divide(LinearPolynomialNode second) {
        if(eitherHasBoundValues(this, second)) {
            throw new PolynomialOperationException("This software does not support division with linear equations");
        }

        double divisionResult = valueOrZero(this.getFreeValue()) / valueOrZero(second.getFreeValue());
        return new LinearPolynomialNode(divisionResult, null);
    }

    public LinearPolynomialNode logarithm() {
        if(this.getBoundValue().isPresent()) {
            throw new PolynomialOperationException("Only linear equations are supported by this software");
        }

        if(!this.getFreeValue().isPresent()) {
            throw new PolynomialOperationException("Logarithm operation requires an argument");
        }

        return new LinearPolynomialNode(Math.log(this.getFreeValue().get()), null);
    }

    private boolean eitherHasFreeValues(LinearPolynomialNode first, LinearPolynomialNode second) {
        return first.getFreeValue().isPresent() || second.getFreeValue().isPresent();
    }

    private boolean eitherHasBoundValues(LinearPolynomialNode first, LinearPolynomialNode second) {
        return first.getBoundValue().isPresent() || second.getBoundValue().isPresent();
    }

    private boolean bothHaveBoundValues(LinearPolynomialNode first, LinearPolynomialNode second) {
        return first.getBoundValue().isPresent() && second.getBoundValue().isPresent();
    }

    private double valueOrZero(Optional<Double> value) {
        return value.orElse(0.0);
    }

    @Override
    public String toString() {
        return "(" + valueOrZero(this.getBoundValue()) + "x" +
                ") + (" + valueOrZero(this.getFreeValue()) + ")";
    }
}
