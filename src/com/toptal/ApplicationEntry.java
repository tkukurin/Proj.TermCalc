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

public class ApplicationEntry {

    public static void main(String[] args) throws IOException {
        CalculatorEnvironment environment = new CalculatorEnvironment(">", new Scanner(System.in), new OutputStreamWriter(System.out));

        List<StringToTokenConverter> stringToTokenConverters = DefaultTokenizerBundle.createTokenConverters();
        TokenizerFactory tokenizerFactory = new TokenizerFactory(stringToTokenConverters, StartToken::new);

        Map<Class<? extends Token>, InfixTransformer> tokenParsingMap = DefaultInfixTransformerBundle.createTokenParsingMap();
        InfixToReversePolishTransformer infixToReversePolishTransformer =
                new InfixToReversePolishTransformer(tokenizerFactory, tokenParsingMap);

        QueryParser queryParser = new QueryParser(new LinearEquationSolver(), infixToReversePolishTransformer);
        String applicationExitString = "exit";

        Application application = new Application(environment, queryParser, applicationExitString);
        application.run();
    }

}
