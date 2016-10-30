package com.toptal.parser;

import java.util.Optional;

public class LinearPolynomialNode {
    private Double freeValue;
    private Double valueBoundToX;

    public LinearPolynomialNode(Double freeValue,
                                Double valueBoundToX) {
        this.freeValue = freeValue;
        this.valueBoundToX = valueBoundToX;
    }

    public Optional<Double> getFreeValue() {
        return Optional.ofNullable(freeValue);
    }

    public Optional<Double> getBoundValue() {
        return Optional.ofNullable(valueBoundToX);
    }

}
