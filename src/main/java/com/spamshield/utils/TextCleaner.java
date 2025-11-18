package com.spamshield.utils;

import java.util.HashSet;
import java.util.Set;

public class TextCleaner {

    private static final Set<String> STOPWORDS = new HashSet<>(Set.of(
        "a","an","the","is","are","and","or","but","if","of","to","in","for","on","with","this","that","it","as","at","by","from"
    ));

    public static String clean(String text) {
        if (text == null) {
            return "";
        }
        String cleaned = text.toLowerCase().replaceAll("[^a-z\\s]", " ").replaceAll("\\s+", " ").trim();
        return cleaned;
    }

    public static boolean isStopword(String token) {
        return STOPWORDS.contains(token);
    }
}
