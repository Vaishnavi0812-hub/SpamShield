package com.spamshield.utils;

import java.util.Set;

public class TextCleaner {

    private static final Set<String> STOPWORDS = Set.of(
        "the","is","am","are","was","were","to","of","and","in","for","on",
        "you","your","we","us","our","they","them","their","a","an","this",
        "that","be","with","as","at","it"
    );

    public static String clean(String text) {
        if (text == null) return "";
        text = text.toLowerCase();
        text = text.replaceAll("[^a-z0-9 ]+", " ");
        text = text.replaceAll("\\s+", " ").trim();
        return text;
    }

    public static boolean isStopword(String w) {
        return STOPWORDS.contains(w);
    }
}
