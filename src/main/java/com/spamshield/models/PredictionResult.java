package com.spamshield.models;

public class PredictionResult {
    private final String prediction;
    private final double spamScore;
    private final double hamScore;

    public PredictionResult(String prediction, double spamScore, double hamScore) {
        this.prediction = prediction;
        this.spamScore = spamScore;
        this.hamScore = hamScore;
    }

    public String getPrediction() {
        return prediction;
    }

    public double getSpamScore() {
        return spamScore;
    }

    public double getHamScore() {
        return hamScore;
    }
}
