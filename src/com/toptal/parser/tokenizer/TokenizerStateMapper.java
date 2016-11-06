package com.toptal.parser.tokenizer;

import com.toptal.parser.tokenizer.tokens.Token;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class TokenizerStateMapper {

    private final BiPredicate<Character, Token> currentCharacterAndTokenPredicate;
    private final BiFunction<String, Integer, Integer> endingPositionFinder;
    private final Function<String, Token> newTokenCreator;

    public TokenizerStateMapper(BiPredicate<Character, Token> currentCharacterAndTokenPredicate,
                                BiFunction<String, Integer, Integer> endingPositionFinder,
                                Function<String, Token> newTokenCreator) {
        this.currentCharacterAndTokenPredicate = currentCharacterAndTokenPredicate;
        this.endingPositionFinder = endingPositionFinder;
        this.newTokenCreator = newTokenCreator;
    }

    boolean accepts(char currentCharacter, Token lastReadToken) {
        return currentCharacterAndTokenPredicate.test(currentCharacter, lastReadToken);
    }

    int findEndingPosition(String input, int position) {
        return endingPositionFinder.apply(input, position);
    }

    Token createToken(String tokenRepresentationString) {
        return newTokenCreator.apply(tokenRepresentationString);
    }

}
