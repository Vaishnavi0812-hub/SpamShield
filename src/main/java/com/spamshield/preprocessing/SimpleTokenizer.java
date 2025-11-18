package com.spamshield.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleTokenizer implements Tokenizer {

    @Override
    public List<String> tokenize(String text) {
        if (text == null) {
            return List.of();
        }
        // convert to lowercase, remove non-letters, split on whitespace
        String cleaned = text.toLowerCase().replaceAll("[^a-z\\s]", " ").replaceAll("\\s+", " ").trim();
        if (cleaned.isEmpty()) {
            return List.of();
        }
        String[] parts = cleaned.split(" ");
        return new ArrayList<>(Arrays.asList(parts));
    }
}
