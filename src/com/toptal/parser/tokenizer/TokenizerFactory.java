package com.toptal.parser.tokenizer;

import com.toptal.parser.tokenizer.tokens.Token;

import java.util.List;
import java.util.function.Supplier;

public class TokenizerFactory {

    private final List<TokenizerStateMapper> tokenizerStateMappers;
    private final Supplier<Token> initialTokenSupplier;

    public TokenizerFactory(List<TokenizerStateMapper> tokenizerStateMappers,
                            Supplier<Token> initialTokenSupplier) {
        this.tokenizerStateMappers = tokenizerStateMappers;
        this.initialTokenSupplier = initialTokenSupplier;
    }

    public Tokenizer createTokenizer(String input) {
        return new Tokenizer(input, initialTokenSupplier.get(), tokenizerStateMappers);
    }

}
