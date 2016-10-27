package com.toptal.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Operation {

    static final Map<String, BiFunction<Double, Double, Double>> binaryOperatorStringToExecutor;
    static {
        binaryOperatorStringToExecutor = new HashMap<>();
        binaryOperatorStringToExecutor.put("+", (a, b) -> a + b);
        binaryOperatorStringToExecutor.put("-", (a, b) -> a - b);
        binaryOperatorStringToExecutor.put("/", (a, b) -> a / b);
        binaryOperatorStringToExecutor.put("*", (a, b) -> a * b);
    }

    static final Map<String, Function<Double, Double>> unaryOperatorToExecutor;
    static {
        unaryOperatorToExecutor = new HashMap<>();
        unaryOperatorToExecutor.put("log", Math::log);
    }

}
