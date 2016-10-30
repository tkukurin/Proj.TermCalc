package com.toptal;

import com.toptal.environment.CalculatorEnvironment;
import com.toptal.parser.QueryParser;
import com.toptal.parser.exception.QueryParseException;

import java.io.IOException;
import java.util.Objects;

public class Application {

    private final CalculatorEnvironment environment;
    private final QueryParser parser;
    private final String applicationExitString;

    public Application(CalculatorEnvironment environment,
                       QueryParser parser,
                       String applicationExitString) {
        this.environment = environment;
        this.parser = parser;
        this.applicationExitString = applicationExitString;
    }

    public void run() throws IOException {
        while (true) {
            String input = environment.requestUserInput();

            if(isExitString(input)) {
                break;
            }

            String result = getResult(input);
            environment.outputLine(result);
        }
    }

    private boolean isExitString(String input) {
        return Objects.equals(applicationExitString, input.trim());
    }

    private String getResult(String input) {
        try {
            return parser.parse(input).toString();
        } catch(QueryParseException e) {
            return "Malformed query: " + e.getLocalizedMessage();
        } catch(Exception e) {
            return "Error evaluating: " + e.getMessage();
        }
    }

}
