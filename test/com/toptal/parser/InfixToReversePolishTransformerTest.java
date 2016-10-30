package com.toptal.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class InfixToReversePolishTransformerTest {

    @Test
    public void shouldProperlyTokenizeDoubleValues() throws Exception {
        // given
        String givenInput = "12.33 + 25.667812";

        // when
        List<String> tokens = InfixToReversePolishTransformer.parse(givenInput);

        // then
        assertContainsInOrder(tokens, "12.33", "25.667812", "+");
    }

    @Test
    public void shouldProperlyParsePrecedence() throws Exception {
        // given
        String givenInput = "1 + 2 * 3";

        // when
        List<String> tokens = InfixToReversePolishTransformer.parse(givenInput);

        // then
        assertContainsInOrder(tokens, "1", "2", "3", "*", "+");
    }

    @Test
    public void shouldProperlyParseParentheses() throws Exception {
        // given
        String givenInput = "(1 + 2) * 3";

        // when
        List<String> tokens = InfixToReversePolishTransformer.parse(givenInput);

        // then
        assertContainsInOrder(tokens, "1", "2", "+", "3", "*");
    }

    @Test
    public void shouldProperlyParseNestedParentheses() throws Exception {
        // given
        String givenInput = "((1 + 2) * 3) / (4 - 7)";

        // when
        List<String> tokens = InfixToReversePolishTransformer.parse(givenInput);

        // then
        assertContainsInOrder(tokens, "1", "2", "+", "3", "*", "4", "7", "-", "/");
    }

    private <T> void assertContainsInOrder(List<T> tokens, T ... values) {
        Assert.assertArrayEquals(tokens.toArray(), values);
    }
}
