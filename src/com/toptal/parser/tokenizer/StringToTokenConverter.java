package com.toptal.parser.tokenizer;

import com.toptal.parser.tokenizer.tokens.Token;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class StringToTokenConverter {

    // starting
    private final Predicate<Character> predicate;

    // String input, character currentPosition -> ending
    private final BiFunction<String, Integer, Integer> consumer;

    // representation, new token
    private final Function<String, Token> tokenCreator;

    public StringToTokenConverter(Predicate<Character> predicate,
                                  BiFunction<String, Integer, Integer> consumer,
                                  Function<String, Token> tokenCreator) {
        this.predicate = predicate;
        this.consumer = consumer;
        this.tokenCreator = tokenCreator;
    }

    public boolean accepts(char c) {
        return predicate.test(c);
    }

    public int findEndingPosition(String input, int position) {
        return consumer.apply(input, position);
    }

    public Token createToken(String s) {
        return tokenCreator.apply(s);
    }

}
