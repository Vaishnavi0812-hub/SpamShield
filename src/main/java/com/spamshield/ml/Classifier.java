package com.spamshield.ml;

import com.spamshield.models.PredictionResult;

public abstract class Classifier {
    /**
     * Train classifier using dataset path (CSV).
     */
    public abstract void train(String datasetPath);

    /**
     * Predict and return scores.
     */
    public abstract PredictionResult predictWithScores(String text);

    /**
     * Convenience: just prediction label.
     */
    public String predict(String text) {
        return predictWithScores(text).getPrediction();
    }
}
