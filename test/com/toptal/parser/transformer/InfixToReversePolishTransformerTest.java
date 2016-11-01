package com.toptal.parser.transformer;

import com.toptal.DefaultInfixTransformerBundle;
import com.toptal.DefaultTokenizerBundle;
import com.toptal.parser.tokenizer.TokenizerFactory;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.operators.*;
import com.toptal.parser.tokenizer.tokens.state.StartToken;
import com.toptal.parser.tokenizer.tokens.values.NumberToken;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.toptal.Helpers.assertContainsInOrder;

public class InfixToReversePolishTransformerTest {

    @Test
    public void shouldProperlyParsePrecedence() throws Exception {
        // given
        String givenInput = "1 + 2 * 3 / 4 - 5";
        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        InfixToReversePolishTokenTransformMap givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();

        // when
        List<String> tokens = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap)
                .tokenizeToReversePolish(givenInput)
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens, "1", "2", "3", "*", "4", "/", "+", "5", "-");
    }

    @Test
    public void shouldProperlyParseNestedParentheses() throws Exception {
        // given
        String givenInput = "((1 + 2) * 3) / (4 - 7)";
        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        InfixToReversePolishTokenTransformMap givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();

        // when
        List<String> tokens = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap)
                .tokenizeToReversePolish(givenInput)
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens, "1", "2", "+", "3", "*", "4", "7", "-", "/");
    }

    @Test
    public void shouldProperlyOrderUnaryValues() throws Exception {
        // given
        String givenInput = "(-2) * -3 + -5 / +2 - -7";
        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        InfixToReversePolishTokenTransformMap givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();

        // when
        List<Class<?>> tokens = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap)
                .tokenizeToReversePolish(givenInput)
                .stream()
                .map(Token::getClass)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens,
                NumberToken.class,
                MinusUnaryOperatorToken.class,
                NumberToken.class,
                MinusUnaryOperatorToken.class,
                MultiplicationBinaryOperatorToken.class,
                NumberToken.class,
                MinusUnaryOperatorToken.class,
                NumberToken.class,
                PlusUnaryOperatorToken.class,
                DivisionBinaryOperatorToken.class,
                AdditionBinaryOperatorToken.class,
                NumberToken.class,
                MinusUnaryOperatorToken.class,
                SubtractionBinaryOperatorToken.class);
    }

}
