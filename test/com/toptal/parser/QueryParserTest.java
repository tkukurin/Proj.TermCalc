package com.toptal.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    private static final double EPSILON = 10e-9;

    @Test
    public void shouldParseSimple() throws Exception {
        // given
        String givenInput = "1 + 2 / 3";

        // when
        LinearPolynomialNode result = QueryParser.parse(givenInput);

        // then
        assertTrue(result.getFreeValue().isPresent());
        assertFalse(result.getBoundValue().isPresent());

        assertEquals(1.0 + 2.0 / 3.0, result.getFreeValue().get(), EPSILON);
    }

    @Test
    public void shouldParseNestedParentheses() throws Exception {
        // given
        String givenInput = "(1.22 + 2) / ((3.1111223 - 7) * 2)";

        // when
        LinearPolynomialNode result = QueryParser.parse(givenInput);

        // then
        assertTrue(result.getFreeValue().isPresent());
        assertFalse(result.getBoundValue().isPresent());

        assertEquals((1.22 + 2) / ((3.1111223 - 7) * 2), result.getFreeValue().get(), EPSILON);
    }
}
