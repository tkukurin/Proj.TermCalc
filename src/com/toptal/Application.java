package com.toptal;

import com.toptal.environment.CalculatorEnvironment;
import com.toptal.parser.QueryParseException;
import com.toptal.parser.QueryParser;

import java.io.IOException;

public class Application {

    private final CalculatorEnvironment environment;
    private final QueryParser parser;

    public Application(CalculatorEnvironment environment,
                       QueryParser parser) {
        this.environment = environment;
        this.parser = parser;
    }

    public void run() throws IOException {
        while (true) {
            String input = environment.requestUserInput();

            if(environment.isExitString(input)) {
                break;
            }

            String result = getResult(input);
            environment.outputLine(result);
        }
    }

    private String getResult(String input) {
        try {
            return parser.parse(input);
        } catch(QueryParseException e) {
            return e.getLocalizedMessage();
        }
    }

    public static void main(String[] args) {

    }

}
