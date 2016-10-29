package com.toptal.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinearPolynomialNodeOperationsTest {

    @Test
    public void shouldPerformAddition() throws Exception {
        // given
        LinearPolynomialNode first = new LinearPolynomialNode(null, 2.0);
        LinearPolynomialNode second = new LinearPolynomialNode(1.0, 2.0);

        // when
        LinearPolynomialNodeOperation operation = new LinearPolynomialNodeOperation();
        LinearPolynomialNode result = operation.add(first, second);

        // then
        assertEquals(Double.valueOf(1.0), result.getFreeValue().get());
        assertEquals(Double.valueOf(4.0), result.getBoundValue().get());
    }

    @Test
    public void shouldPerformMultiplicationWithBoundVariables() throws Exception {
        // given
        LinearPolynomialNode first = new LinearPolynomialNode(2.0, null);
        LinearPolynomialNode second = new LinearPolynomialNode(1.0, 2.0);

        // when
        LinearPolynomialNodeOperation operation = new LinearPolynomialNodeOperation();
        LinearPolynomialNode result = operation.mutliply(first, second);

        // then
        assertEquals(Double.valueOf(2.0), result.getFreeValue().get());
        assertEquals(Double.valueOf(4.0), result.getBoundValue().get());
    }

    @Test(expected = QueryParseException.class)
    public void shouldThrowExceptionIfEquationIsNonLinear() throws Exception {
        // given
        LinearPolynomialNode first = new LinearPolynomialNode(2.0, 3.0);
        LinearPolynomialNode second = new LinearPolynomialNode(1.0, 2.0);

        // when
        LinearPolynomialNodeOperation operation = new LinearPolynomialNodeOperation();
        operation.mutliply(first, second);

        // then throws exception
    }

    @Test
    public void shouldPerformDivisionOnUnboundVariables() throws Exception {
        // given
        LinearPolynomialNode first = new LinearPolynomialNode(2.0, null);
        LinearPolynomialNode second = new LinearPolynomialNode(5.0, null);

        // when
        LinearPolynomialNodeOperation operation = new LinearPolynomialNodeOperation();
        LinearPolynomialNode result = operation.divide(first, second);

        // then
        assertEquals(Double.valueOf(2.0 / 5.0), result.getFreeValue().get());
    }

    @Test(expected = QueryParseException.class)
    public void shouldThrowExceptionOnBoundVariableDivision() throws Exception {
        // given
        LinearPolynomialNode first = new LinearPolynomialNode(2.0, null);
        LinearPolynomialNode second = new LinearPolynomialNode(1.0, 2.0);

        // when
        LinearPolynomialNodeOperation operation = new LinearPolynomialNodeOperation();
        operation.divide(first, second);

        // then throws exception
    }

    @Test
    public void shouldTakeLogarithm() throws Exception {
        // given
        LinearPolynomialNode node = new LinearPolynomialNode(2.0, null);

        // when
        LinearPolynomialNodeOperation operation = new LinearPolynomialNodeOperation();
        LinearPolynomialNode result = operation.logarithm(node);

        // then
        assertEquals(Double.valueOf(Math.log(2.0)), result.getFreeValue().get());
    }

    @Test(expected = QueryParseException.class)
    public void shouldThrowExceptionIfTakingLogarithmOfBoundVariable() throws Exception {
        // given
        LinearPolynomialNode node = new LinearPolynomialNode(2.0, 1.0);

        // when
        LinearPolynomialNodeOperation operation = new LinearPolynomialNodeOperation();
        operation.logarithm(node);

        // then throws exception
    }
}
