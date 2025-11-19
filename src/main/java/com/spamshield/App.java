package com.spamshield;
import com.spamshield.api.ApiServer;
import com.spamshield.ml.NaiveBayesClassifier;
import com.spamshield.preprocessing.SimpleTokenizer;
import com.spamshield.preprocessing.Tokenizer;

public class App {
    public static void main(String[] args) {

        Tokenizer tokenizer = new SimpleTokenizer();
        NaiveBayesClassifier nb = new NaiveBayesClassifier(tokenizer);

        // STEP 3 — Train the model
        nb.train("emails.csv");

        // Start API Server
        ApiServer.start(nb);
    }
}
