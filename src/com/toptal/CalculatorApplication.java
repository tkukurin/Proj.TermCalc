package com.toptal;

import com.toptal.environment.CalculatorEnvironment;
import com.toptal.parser.QueryParser;
import com.toptal.parser.exception.EquationSolveException;
import com.toptal.parser.exception.PolynomialOperationException;
import com.toptal.parser.exception.QueryParseException;

import java.io.IOException;
import java.util.Objects;

class CalculatorApplication {

    private final CalculatorEnvironment environment;
    private final QueryParser parser;
    private final String applicationExitString;

    CalculatorApplication(CalculatorEnvironment environment,
                          QueryParser parser,
                          String applicationExitString) {
        this.environment = environment;
        this.parser = parser;
        this.applicationExitString = applicationExitString;
    }

    void run() throws IOException {
        while (true) {
            String input = environment.requestUserInput();

            if (isExitString(input)) {
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
        } catch (QueryParseException e) {
            return "Malformed query: " + e.getLocalizedMessage();
        } catch (PolynomialOperationException e) {
            return "Error applying operation: " + e.getLocalizedMessage();
        } catch (EquationSolveException e) {
            return "Error solving linear equation: " + e.getLocalizedMessage();
        } catch (Exception e) {
            return "Runtime error: " + e.getMessage();
        }
    }

}
