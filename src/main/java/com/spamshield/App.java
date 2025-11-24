package com.spamshield;

import com.spamshield.api.ApiServer;
import com.spamshield.ml.NaiveBayesClassifier;
import com.spamshield.preprocessing.SimpleTokenizer;
import com.spamshield.preprocessing.Tokenizer;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting SpamShield...");

        Tokenizer tokenizer = new SimpleTokenizer();
        NaiveBayesClassifier nb = new NaiveBayesClassifier(tokenizer);

        // Train model using emails.csv which is packaged under src/main/resources
        // We pass the resource name — FileUtils.load will read from classpath
        nb.train("emails.csv");
        System.out.println("Training completed.");

        // Start REST API and pass the trained classifier
        ApiServer.start(nb);
        System.out.println("API started at http://localhost:4567");
    }
}
