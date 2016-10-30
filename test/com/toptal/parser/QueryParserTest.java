package com.toptal.parser;

public class QueryParserTest {

    private static final double EPSILON = 10e-9;

//    @Test
//    public void shouldParseSimple() throws Exception {
//        // given
//        String givenInput = "1 + 2 / 3";
//
//        // when
//        LinearPolynomialNode result = QueryParser._parse(givenInput);
//
//        // then
//        assertTrue(result.getFreeValue().isPresent());
//        assertFalse(result.getBoundValue().isPresent());
//
//        assertEquals(1.0 + 2.0 / 3.0, result.getFreeValue().get(), EPSILON);
//    }
//
//    @Test
//    public void shouldParseNestedParentheses() throws Exception {
//        // given
//        String givenInput = "(1.22 + 2) / ((3.1111223 - 7) * 2)";
//
//        // when
//        LinearPolynomialNode result = QueryParser._parse(givenInput);
//
//        // then
//        assertTrue(result.getFreeValue().isPresent());
//        assertFalse(result.getBoundValue().isPresent());
//
//        assertEquals((1.22 + 2) / ((3.1111223 - 7) * 2), result.getFreeValue().get(), EPSILON);
//    }
//
//    @Test
//    public void shouldHandleCaseWithNoMultiplicationSign() throws Exception {
//        // given
//        String givenInput = "24(1 - 19)";
//
//        // when
//        LinearPolynomialNode result = QueryParser._parse(givenInput);
//
//        // then
//        assertTrue(result.getFreeValue().isPresent());
//        assertFalse(result.getBoundValue().isPresent());
//
//        assertEquals(24 * (1 - 19), result.getFreeValue().get(), EPSILON);
//    }
//
//    @Test
//    public void shouldHandleCaseWithTwoParentheses() throws Exception {
//        // given
//        String givenInput = "(1+2)(22+3)";
//
//        // when
//        LinearPolynomialNode result = QueryParser._parse(givenInput);
//
//        // then
//        assertTrue(result.getFreeValue().isPresent());
//        assertFalse(result.getBoundValue().isPresent());
//
//        assertEquals((1+2) * (22+3), result.getFreeValue().get(), EPSILON);
//    }
//
//    @Test
//    public void shouldHandleImplicitUnaryMinus() throws Exception {
//        // given
//        String givenInput = "-3 + (-2)";
//
//        // when
//        LinearPolynomialNode result = QueryParser._parse(givenInput);
//
//        // then
//        assertTrue(result.getFreeValue().isPresent());
//        assertFalse(result.getBoundValue().isPresent());
//
//        assertEquals(-3 - 2, result.getFreeValue().get(), EPSILON);
//    }
//
//    @Test
//    public void shouldHandleImplicitUnaryMinusOnVariable() throws Exception {
//        // given
//        String givenInput = "-x + 2";
//
//        // when
//        LinearPolynomialNode result = QueryParser._parse(givenInput);
//
//        // then
//        assertTrue(result.getFreeValue().isPresent());
//        assertTrue(result.getBoundValue().isPresent());
//
//        assertEquals(2, result.getFreeValue().get(), EPSILON);
//        assertEquals(-1, result.getBoundValue().get(), EPSILON);
//    }
}
