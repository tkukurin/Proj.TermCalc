package com.toptal.parser;

import com.toptal.DefaultInfixTransformerBundle;
import com.toptal.DefaultTokenizerBundle;
import com.toptal.parser.tokenizer.TokenizerFactory;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.operators.MinusUnaryOperatorToken;
import com.toptal.parser.tokenizer.tokens.state.StartToken;
import com.toptal.parser.tokenizer.tokens.values.NumberToken;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InfixToReversePolishTransformerTest {

    @Test
    public void shouldProperlyTokenizeDoubleValues() throws Exception {
        // given
        String givenInput = "12.33 + 25.667812";
        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        Map<Class<? extends Token>, InfixToReversePolishTokenTransformer> givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();

        // when
        List<String> tokens = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap)
                .parse(givenInput)
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens, "12.33", "25.667812", "+");
    }

    @Test
    public void shouldProperlyParsePrecedence() throws Exception {
        // given
        String givenInput = "1 + 2 * 3";
        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        Map<Class<? extends Token>, InfixToReversePolishTokenTransformer> givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();

        // when
        List<String> tokens = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap)
                .parse(givenInput)
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens, "1", "2", "3", "*", "+");
    }

    @Test
    public void shouldProperlyParseParentheses() throws Exception {
        // given
        String givenInput = "(1 + 2) * 3";
        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        Map<Class<? extends Token>, InfixToReversePolishTokenTransformer> givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();

        // when
        List<String> tokens = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap)
                .parse(givenInput)
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens, "1", "2", "+", "3", "*");
    }

    @Test
    public void shouldProperlyParseNestedParentheses() throws Exception {
        // given
        String givenInput = "((1 + 2) * 3) / (4 - 7)";
        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        Map<Class<? extends Token>, InfixToReversePolishTokenTransformer> givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();

        // when
        List<String> tokens = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap)
                .parse(givenInput)
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens, "1", "2", "+", "3", "*", "4", "7", "-", "/");
    }

    @Test
    public void shouldHandleCaseWithNoMultiplicationSignAndParentheses() throws Exception {
        // given
        String givenInput = "2(1+2)";
        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        Map<Class<? extends Token>, InfixToReversePolishTokenTransformer> givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();

        // when
        List<String> tokens = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap)
                .parse(givenInput)
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens, "2", "1", "2", "+", "*");
    }

    @Test
    public void shouldHandleCaseWithNoMultiplicationSignAndX() throws Exception {
        // given
        String givenInput = "2(2x + 2)";
        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        Map<Class<? extends Token>, InfixToReversePolishTokenTransformer> givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();

        // when
        List<String> tokens = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap)
                .parse(givenInput)
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens, "2", "2", "x", "*", "2", "+", "*");
    }

    @Test
    public void shouldHandleUnaryValues() throws Exception {
        // given
        String givenInput = "(-2)";
        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        Map<Class<? extends Token>, InfixToReversePolishTokenTransformer> givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();

        // when
        List<Class<?>> tokens = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap)
                .parse(givenInput)
                .stream()
                .map(Token::getClass)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens, NumberToken.class, MinusUnaryOperatorToken.class);
    }

    private <T> void assertContainsInOrder(List<T> tokens, T ... values) {
        Assert.assertArrayEquals(tokens.toArray(), values);
    }

}
