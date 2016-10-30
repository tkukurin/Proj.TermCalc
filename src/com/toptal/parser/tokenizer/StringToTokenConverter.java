package com.toptal.parser.tokenizer;

import com.toptal.parser.tokenizer.tokens.Token;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class StringToTokenConverter {

    private final BiPredicate<Character, Token> currentCharacterAndTokenPredicate;
    private final BiFunction<String, Integer, Integer> endingPositionFinder;
    private final Function<String, Token> newTokenCreator;

    public StringToTokenConverter(BiPredicate<Character, Token> currentCharacterAndTokenPredicate,
                                  BiFunction<String, Integer, Integer> endingPositionFinder,
                                  Function<String, Token> newTokenCreator) {
        this.currentCharacterAndTokenPredicate = currentCharacterAndTokenPredicate;
        this.endingPositionFinder = endingPositionFinder;
        this.newTokenCreator = newTokenCreator;
    }

    boolean accepts(char c, Token token) {
        return currentCharacterAndTokenPredicate.test(c, token);
    }

    int findEndingPosition(String input, int position) {
        return endingPositionFinder.apply(input, position);
    }

    Token createToken(String s) {
        return newTokenCreator.apply(s);
    }

}
