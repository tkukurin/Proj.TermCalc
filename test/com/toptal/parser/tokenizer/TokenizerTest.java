package com.toptal.parser.tokenizer;

import com.toptal.DefaultTokenizerBundle;
import com.toptal.parser.exceptions.QueryParseException;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.operators.*;
import com.toptal.parser.tokenizer.tokens.state.CloseParenthesisToken;
import com.toptal.parser.tokenizer.tokens.state.OpenParenthesisToken;
import com.toptal.parser.tokenizer.tokens.state.StartToken;
import com.toptal.parser.tokenizer.tokens.values.NumberToken;
import com.toptal.parser.tokenizer.tokens.values.VariableToken;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.toptal.Helpers.assertContainsInOrder;

public class TokenizerTest {

    @Test
    public void shouldProperlyTokenizeNumberValues() throws Exception {
        // given
        String givenInput = "12.33 + 25.667812 + 445";
        LinkedList<TokenizerStateMapper> givenTokenConverters = DefaultTokenizerBundle.createTokenConverters();

        // when
        List<String> tokens = extractAllTokens(new Tokenizer(givenInput, new StartToken(), givenTokenConverters))
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens, "12.33", "+", "25.667812", "+", "445");
    }

    @Test
    public void shouldInsertImplicitMultiplicationSign() throws Exception {
        // given
        String givenInput = "2(5)(3+1)log(3)x";
        LinkedList<TokenizerStateMapper> givenTokenConverters = DefaultTokenizerBundle.createTokenConverters();

        // when
        List<String> tokens = extractAllTokens(new Tokenizer(givenInput, new StartToken(), givenTokenConverters))
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens, "2", "*", "(", "5", ")", "*", "(", "3", "+", "1", ")", "*", "log", "(", "3", ")", "*", "x");
    }

    @Test
    public void shouldRecognizeUnaryValues() throws Exception {
        // given
        String givenInput = "(-3) + +3.5 * -x / -log(4)";
        LinkedList<TokenizerStateMapper> givenTokenConverters = DefaultTokenizerBundle.createTokenConverters();

        // when
        List<Class<?>> tokens = extractAllTokens(new Tokenizer(givenInput, new StartToken(), givenTokenConverters))
                .stream()
                .map(Token::getClass)
                .collect(Collectors.toList());

        // then
        assertContainsInOrder(tokens,
                OpenParenthesisToken.class,
                MinusUnaryOperatorToken.class,
                NumberToken.class,
                CloseParenthesisToken.class,
                AdditionBinaryOperatorToken.class,
                PlusUnaryOperatorToken.class,
                NumberToken.class,
                MultiplicationBinaryOperatorToken.class,
                MinusUnaryOperatorToken.class,
                VariableToken.class,
                DivisionBinaryOperatorToken.class,
                MinusUnaryOperatorToken.class,
                LogarithmFunctionToken.class,
                OpenParenthesisToken.class,
                NumberToken.class,
                CloseParenthesisToken.class);
    }

    @Test(expected = QueryParseException.class)
    public void shouldRequireParenthesisedFunctions() throws Exception {
        // given
        String givenInput = "log 2";
        LinkedList<TokenizerStateMapper> givenTokenConverters = DefaultTokenizerBundle.createTokenConverters();

        // when
        extractAllTokens(new Tokenizer(givenInput, new StartToken(), givenTokenConverters))
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());

        // then throws exception
    }

    @Test(expected = QueryParseException.class)
    public void shouldThrowExceptionIfUnknownCharacterEncountered() throws Exception {
        // given
        String givenInput = "123.56 + 22 * 743 / a";
        LinkedList<TokenizerStateMapper> givenTokenConverters = DefaultTokenizerBundle.createTokenConverters();

        // when
        extractAllTokens(new Tokenizer(givenInput, new StartToken(), givenTokenConverters))
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());

        // then throws exception
    }

    private List<Token> extractAllTokens(Tokenizer tokenizer) {

        List<Token> tokens = new LinkedList<>();
        Token current = tokenizer.next();

        while(!current.isEndingState()) {
            tokens.add(current);
            current = tokenizer.next();
        }

        return tokens;
    }

}
