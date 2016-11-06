package com.toptal;

import com.toptal.environment.CalculatorEnvironment;
import com.toptal.parser.LinearPolynomialNodeValueTransformer;
import com.toptal.parser.QueryParser;
import com.toptal.parser.tokenizer.TokenizerFactory;
import com.toptal.parser.tokenizer.TokenizerStateMapper;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.state.StartToken;
import com.toptal.parser.transformer.InfixToReversePolishTokenTransformMap;
import com.toptal.parser.transformer.InfixToReversePolishTransformer;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class CalculatorApplicationEntry {

    public static void main(String[] args) throws IOException {
        final CalculatorEnvironment environment = createApplicationEnvironment();
        final QueryParser queryParser = createQueryParser();
        final String applicationExitString = "exit";
        final CalculatorApplication calculatorApplication = new CalculatorApplication(environment, queryParser, applicationExitString);

        calculatorApplication.run();
    }

    private static CalculatorEnvironment createApplicationEnvironment() {
        final String inputPointer = "> ";
        final Scanner userInputScanner = new Scanner(System.in);
        final OutputStreamWriter outputWriter = new OutputStreamWriter(System.out);

        return new CalculatorEnvironment(inputPointer, userInputScanner, outputWriter);
    }

    private static QueryParser createQueryParser() {
        final TokenizerFactory tokenizerFactory = createTokenizerFactory();
        final InfixToReversePolishTransformer infixToReversePolishTransformer = createInfixToReversePolishTransformer(tokenizerFactory);
        final LinearPolynomialNodeValueTransformer linearPolynomialNodeValueTransformer = new LinearPolynomialNodeValueTransformer();

        return new QueryParser(linearPolynomialNodeValueTransformer, infixToReversePolishTransformer);
    }

    private static TokenizerFactory createTokenizerFactory() {
        final List<TokenizerStateMapper> tokenizerStateMappers = DefaultTokenizerBundle.createTokenConverters();
        final Supplier<Token> startingToken = StartToken::new;

        return new TokenizerFactory(tokenizerStateMappers, startingToken);
    }

    private static InfixToReversePolishTransformer createInfixToReversePolishTransformer(TokenizerFactory tokenizerFactory) {
        final InfixToReversePolishTokenTransformMap tokenParsingMap = DefaultInfixTransformerBundle.createTokenParsingMap();

        return new InfixToReversePolishTransformer(tokenizerFactory, tokenParsingMap);
    }

}
