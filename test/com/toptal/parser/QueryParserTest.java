package com.toptal.parser;

import com.toptal.DefaultInfixTransformerBundle;
import com.toptal.DefaultTokenizerBundle;
import com.toptal.parser.exceptions.QueryParseException;
import com.toptal.parser.result.QueryParseResult;
import com.toptal.parser.tokenizer.TokenizerFactory;
import com.toptal.parser.tokenizer.tokens.state.StartToken;
import com.toptal.parser.transformer.InfixToReversePolishTokenTransformMap;
import com.toptal.parser.transformer.InfixToReversePolishTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryParserTest {

    private static final double EPSILON = 10e-9;

    @Test
    public void shouldKeepPrecedence() throws Exception {
        // given
        String givenInput = "1 + 2 / 3";

        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        InfixToReversePolishTokenTransformMap givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();
        LinearPolynomialNodeValueTransformer givenLinearPolynomialNodeValueTransformer = new LinearPolynomialNodeValueTransformer();
        InfixToReversePolishTransformer givenInfixToReversePolishTransformer = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap);

        // when
        QueryParseResult result = new QueryParser(givenLinearPolynomialNodeValueTransformer, givenInfixToReversePolishTransformer)
                .parse(givenInput);

        // then
        assertEquals(1.0 + 2.0 / 3.0, result.getSolution(), EPSILON);
    }


    @Test
    public void shouldParseNestedParentheses() throws Exception {
        // given
        String givenInput = "(1.22 + 2) / ((3.1111223 - 7) * 2)";

        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        InfixToReversePolishTokenTransformMap givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();
        LinearPolynomialNodeValueTransformer givenLinearPolynomialNodeValueTransformer = new LinearPolynomialNodeValueTransformer();
        InfixToReversePolishTransformer givenInfixToReversePolishTransformer = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap);

        // when
        QueryParseResult result = new QueryParser(givenLinearPolynomialNodeValueTransformer, givenInfixToReversePolishTransformer)
                .parse(givenInput);

        // then
        assertEquals((1.22 + 2) / ((3.1111223 - 7) * 2), result.getSolution(), EPSILON);
    }

    @Test
    public void shouldHandleImplicitMultiplicationSign() throws Exception {
        // given
        String givenInput = "24(1 - 19)";

        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        InfixToReversePolishTokenTransformMap givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();
        LinearPolynomialNodeValueTransformer givenLinearPolynomialNodeValueTransformer = new LinearPolynomialNodeValueTransformer();
        InfixToReversePolishTransformer givenInfixToReversePolishTransformer = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap);

        // when
        QueryParseResult result = new QueryParser(givenLinearPolynomialNodeValueTransformer, givenInfixToReversePolishTransformer)
                .parse(givenInput);

        // then
        assertEquals(24*(1-19), result.getSolution(), EPSILON);
    }

    @Test
    public void shouldHandleMultipleParenthesesMultiplication() throws Exception {
        // given
        String givenInput = "(1+2)(22+3)(1+2(3+1)+3)";

        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        InfixToReversePolishTokenTransformMap givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();
        LinearPolynomialNodeValueTransformer givenLinearPolynomialNodeValueTransformer = new LinearPolynomialNodeValueTransformer();
        InfixToReversePolishTransformer givenInfixToReversePolishTransformer = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap);

        // when
        QueryParseResult result = new QueryParser(givenLinearPolynomialNodeValueTransformer, givenInfixToReversePolishTransformer)
                .parse(givenInput);

        // then
        assertEquals((1+2)*(22+3)*(1+2*(3+1)+3), result.getSolution(), EPSILON);
    }

    @Test
    public void shouldHandleImplicitUnaryMinus() throws Exception {
        // given
        String givenInput = "-3 + (-2) + -(2+1) = -x";

        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        InfixToReversePolishTokenTransformMap givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();
        LinearPolynomialNodeValueTransformer givenLinearPolynomialNodeValueTransformer = new LinearPolynomialNodeValueTransformer();
        InfixToReversePolishTransformer givenInfixToReversePolishTransformer = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap);

        // when
        QueryParseResult result = new QueryParser(givenLinearPolynomialNodeValueTransformer, givenInfixToReversePolishTransformer)
                .parse(givenInput);

        // then
        assertEquals(8.0, result.getSolution(), EPSILON);
    }

    @Test
    public void shouldHandleImplicitUnaryMinusOnLogarithm() throws Exception {
        // given
        String givenInput = "-log(5)2";

        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        InfixToReversePolishTokenTransformMap givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();
        LinearPolynomialNodeValueTransformer givenLinearPolynomialNodeValueTransformer = new LinearPolynomialNodeValueTransformer();
        InfixToReversePolishTransformer givenInfixToReversePolishTransformer = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap);

        // when
        QueryParseResult result = new QueryParser(givenLinearPolynomialNodeValueTransformer, givenInfixToReversePolishTransformer)
                .parse(givenInput);

        // then
        assertEquals(-2 * Math.log(5), result.getSolution(), EPSILON);
    }

    @Test(expected = QueryParseException.class)
    public void shouldThrowExceptionOnMissingValue() throws Exception {
        // given
        String givenInput = "2 +";

        TokenizerFactory givenTokenizerFactory = new TokenizerFactory(DefaultTokenizerBundle.createTokenConverters(), StartToken::new);
        InfixToReversePolishTokenTransformMap givenParseMap = DefaultInfixTransformerBundle.createTokenParsingMap();
        LinearPolynomialNodeValueTransformer givenLinearPolynomialNodeValueTransformer = new LinearPolynomialNodeValueTransformer();
        InfixToReversePolishTransformer givenInfixToReversePolishTransformer = new InfixToReversePolishTransformer(givenTokenizerFactory, givenParseMap);

        // when
        new QueryParser(givenLinearPolynomialNodeValueTransformer, givenInfixToReversePolishTransformer).parse(givenInput);

        // then throws exceptions
    }

}
