package com.spamshield.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleTokenizer implements Tokenizer {

    @Override
    public List<String> tokenize(String text) {
        if (text == null) return List.of();
        // lower, remove non-letters and collapse spaces
        String cleaned = text.toLowerCase().replaceAll("[^a-z\\s]", " ").replaceAll("\\s+", " ").trim();
        if (cleaned.isEmpty()) return List.of();
        return new ArrayList<>(Arrays.asList(cleaned.split(" ")));
    }
}
