package com.spamshield.ml;

import com.spamshield.models.PredictionResult;

public abstract class Classifier {
    public abstract void train(String datasetPath);
    public abstract PredictionResult predictWithScores(String text);
}
