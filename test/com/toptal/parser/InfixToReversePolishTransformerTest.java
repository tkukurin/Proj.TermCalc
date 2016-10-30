package com.toptal.parser;

public class InfixToReversePolishTransformerTest {

//    @Test
//    public void shouldProperlyTokenizeDoubleValues() throws Exception {
//        // given
//        String givenInput = "12.33 + 25.667812";
//
//        // when
//        List<String> tokens = new InfixToReversePolishTransformer(tokenizerFactory, tokenToOperationMap, operatorToPrecedenceMap).parse(givenInput)
//                .stream()
//                .map(Token::getRepresentation)
//                .collect(Collectors.toList());
//
//        // then
//        assertContainsInOrder(tokens, "12.33", "25.667812", "+");
//    }
//
//    @Test
//    public void shouldProperlyParsePrecedence() throws Exception {
//        // given
//        String givenInput = "1 + 2 * 3";
//
//        // when
//        List<String> tokens = new InfixToReversePolishTransformer(tokenizerFactory, tokenToOperationMap, operatorToPrecedenceMap).parse(givenInput)
//                .stream()
//                .map(Token::getRepresentation)
//                .collect(Collectors.toList());
//
//        // then
//        assertContainsInOrder(tokens, "1", "2", "3", "*", "+");
//    }
//
//    @Test
//    public void shouldProperlyParseParentheses() throws Exception {
//        // given
//        String givenInput = "(1 + 2) * 3";
//
//        // when
//        List<String> tokens = new InfixToReversePolishTransformer(tokenizerFactory, tokenToOperationMap, operatorToPrecedenceMap).parse(givenInput)
//                .stream()
//                .map(Token::getRepresentation)
//                .collect(Collectors.toList());
//
//        // then
//        assertContainsInOrder(tokens, "1", "2", "+", "3", "*");
//    }
//
//    @Test
//    public void shouldProperlyParseNestedParentheses() throws Exception {
//        // given
//        String givenInput = "((1 + 2) * 3) / (4 - 7)";
//
//        // when
//        List<String> tokens = new InfixToReversePolishTransformer(tokenizerFactory, tokenToOperationMap, operatorToPrecedenceMap).parse(givenInput)
//                .stream()
//                .map(Token::getRepresentation)
//                .collect(Collectors.toList());
//
//        // then
//        assertContainsInOrder(tokens, "1", "2", "+", "3", "*", "4", "7", "-", "/");
//    }
//
//    @Test
//    public void shouldHandleCaseWithNoMultiplicationSignAndParentheses() throws Exception {
//        // given
//        String givenInput = "2(1+2)";
//
//        // when
//        List<String> tokens = new InfixToReversePolishTransformer(tokenizerFactory, tokenToOperationMap, operatorToPrecedenceMap).parse(givenInput)
//                .stream()
//                .map(Token::getRepresentation)
//                .collect(Collectors.toList());
//
//        // then
//        assertContainsInOrder(tokens, "2", "1", "2", "+", "*");
//    }
//
//    @Test
//    public void shouldHandleCaseWithNoMultiplicationSignAndX() throws Exception {
//        // given
//        String givenInput = "2(2x + 2)";
//
//        // when
//        List<String> tokens = new InfixToReversePolishTransformer(tokenizerFactory, tokenToOperationMap, operatorToPrecedenceMap).parse(givenInput)
//                .stream()
//                .map(Token::getRepresentation)
//                .collect(Collectors.toList());
//
//        // then
//        assertContainsInOrder(tokens, "2", "2", "x", "*", "2", "+", "*");
//    }
//
//    private <T> void assertContainsInOrder(List<T> tokens, T ... values) {
//        Assert.assertArrayEquals(tokens.toArray(), values);
//    }
}
