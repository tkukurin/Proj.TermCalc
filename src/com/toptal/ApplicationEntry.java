package com.toptal;

import com.toptal.environment.CalculatorEnvironment;
import com.toptal.parser.InfixToReversePolishTransformer;
import com.toptal.parser.InfixTransformer;
import com.toptal.parser.LinearEquationSolver;
import com.toptal.parser.QueryParser;
import com.toptal.parser.tokenizer.StringToTokenConverter;
import com.toptal.parser.tokenizer.TokenizerFactory;
import com.toptal.parser.tokenizer.tokens.Token;
import com.toptal.parser.tokenizer.tokens.state.StartToken;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

public class ApplicationEntry {

    public static void main(String[] args) throws IOException {
        final CalculatorEnvironment environment = createApplicationEnvironment();
        final QueryParser queryParser = createQueryParser();
        final String applicationExitString = "exit";
        final Application application = new Application(environment, queryParser, applicationExitString);

        application.run();
    }

    private static QueryParser createQueryParser() {
        final TokenizerFactory tokenizerFactory = createTokenizerFactory();
        final InfixToReversePolishTransformer infixToReversePolishTransformer = createInfixToReversePolishTransformer(tokenizerFactory);
        final LinearEquationSolver linearEquationSolver = new LinearEquationSolver();
        return new QueryParser(linearEquationSolver, infixToReversePolishTransformer);
    }

    private static InfixToReversePolishTransformer createInfixToReversePolishTransformer(TokenizerFactory tokenizerFactory) {
        Map<Class<? extends Token>, InfixTransformer> tokenParsingMap = DefaultInfixTransformerBundle.createTokenParsingMap();
        return new InfixToReversePolishTransformer(tokenizerFactory, tokenParsingMap);
    }

    private static CalculatorEnvironment createApplicationEnvironment() {
        return new CalculatorEnvironment(">", new Scanner(System.in), new OutputStreamWriter(System.out));
    }

    private static TokenizerFactory createTokenizerFactory() {
        List<StringToTokenConverter> stringToTokenConverters = DefaultTokenizerBundle.createTokenConverters();
        Supplier<Token> startingToken = StartToken::new;
        return new TokenizerFactory(stringToTokenConverters, startingToken);
    }

}
