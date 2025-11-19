package com.spamshield.preprocessing;

import java.util.ArrayList;
import java.util.List;

public class SimpleTokenizer implements Tokenizer {

    @Override
    public List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();

        if (text == null) return tokens;

        // split on everything except letters and numbers
        String[] parts = text.toLowerCase().split("[^a-z0-9]+");

        for (String p : parts) {
            if (p.isBlank()) continue;
            tokens.add(p);
        }

        return tokens;
    }
}
