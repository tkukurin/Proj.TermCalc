package com.toptal.parser.tokenizer;

import com.toptal.parser.tokenizer.tokens.Token;

import java.util.List;
import java.util.function.Supplier;

public class TokenizerFactory {

    private final List<StringToTokenConverter> stringToTokenConverters;
    private final Supplier<Token> initialTokenSupplier;

    public TokenizerFactory(List<StringToTokenConverter> stringToTokenConverters,
                            Supplier<Token> initialTokenSupplier) {
        this.stringToTokenConverters = stringToTokenConverters;
        this.initialTokenSupplier = initialTokenSupplier;
    }

    public Tokenizer createTokenizer(String input) {
        return new Tokenizer(input, initialTokenSupplier.get(), stringToTokenConverters);
    }

}
