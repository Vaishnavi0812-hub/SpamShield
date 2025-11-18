package com.spamshield.preprocessing;

import java.util.List;

public interface Tokenizer {
    /**
     * Breaks text into tokens (words).
     */
    List<String> tokenize(String text);
}
