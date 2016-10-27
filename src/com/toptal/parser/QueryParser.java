package com.toptal.parser;

import java.util.function.BiFunction;

import static com.toptal.parser.Operation.binaryOperatorStringToExecutor;

public class QueryParser {

    public String parse(String input) {
        input = input.trim();
        String returnValue = "";

        for(String operator : binaryOperatorStringToExecutor.keySet()) {
            String[] numbers = splitBy(input, operator);

            if(numbers.length == 2) {
                double result = performOperation(numbers[0], numbers[1], binaryOperatorStringToExecutor.get(operator));
                returnValue = Double.toString(result);
                break;
            }
        }


        return returnValue;
    }

    private String[] splitBy(String input, String operator) {
        int operatorIndex = input.indexOf(operator);

        if(operatorIndex >= 0) {
            return new String[] {
                    input.substring(0, operatorIndex).trim(),
                    input.substring(operatorIndex + 1).trim()
            };
        }

        return new String[] { input };
    }

    private double performOperation(String firstOperandString,
                                    String secondOperandString,
                                    BiFunction<Double, Double, Double> operation) {
        double firstOperand = Double.parseDouble(firstOperandString);
        double secondOperand = Double.parseDouble(secondOperandString);

        return operation.apply(firstOperand, secondOperand);
    }

}
