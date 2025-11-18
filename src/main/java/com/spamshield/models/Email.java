package com.spamshield.models;

public class Email {
    private final String label; // "spam" or "ham"
    private final String text;

    public Email(String label, String text) {
        this.label = label;
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public String getText() {
        return text;
    }
}
