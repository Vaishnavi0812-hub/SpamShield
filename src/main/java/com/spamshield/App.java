package com.spamshield;

import com.spamshield.api.ApiServer;
import com.spamshield.ml.NaiveBayesClassifier;
import com.spamshield.preprocessing.SimpleTokenizer;
import com.spamshield.preprocessing.Tokenizer;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting SpamShield...");

        Tokenizer tokenizer = new SimpleTokenizer();
        NaiveBayesClassifier classifier = new NaiveBayesClassifier(tokenizer);

        // **TRAIN** the classifier on your dataset
        // (make sure you have dataset path correctly)
        String datasetPath = "dataset/emails.csv";
        classifier.train(datasetPath);
        System.out.println("Training done.");

        // Pass classifier to API or UI
        ApiServer.start(classifier);

        // (If you build UI later, you will also pass classifier there.)
    }
}
