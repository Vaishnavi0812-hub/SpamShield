package com.spamshield.ml;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.spamshield.models.Email;
import com.spamshield.models.PredictionResult;
import com.spamshield.preprocessing.Tokenizer;
import com.spamshield.utils.TextCleaner;

public class NaiveBayesClassifier extends Classifier {

    private final Tokenizer tokenizer;
    private final TFIDFVectorizer vectorizer;

    private Map<String, Integer> spamCounts = new HashMap<>();
    private Map<String, Integer> hamCounts = new HashMap<>();
    private int totalSpamTokens = 0;
    private int totalHamTokens = 0;
    private int spamDocs = 0;
    private int hamDocs = 0;
    private int vocabSize = 0;

    public NaiveBayesClassifier(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        this.vectorizer = new TFIDFVectorizer(tokenizer);
    }

    @Override
    public void train(String datasetPath) {
        List<Email> data;
        try {
            data = EmailDatasetLoader.load(datasetPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not load dataset: " + e.getMessage(), e);
        }

        // Fit vectorizer
         vectorizer.fit(
        data.stream().map(Email::getText).toList()
    );

        for (Email e : data) {
            List<String> tokens = tokenizer.tokenize(TextCleaner.clean(e.getText()));
            if (e.getLabel().equals("spam")) {
                spamDocs++;
                for (String t : tokens) {
                    if (TextCleaner.isStopword(t)) continue;
                    spamCounts.put(t, spamCounts.getOrDefault(t, 0) + 1);
                    totalSpamTokens++;
                }
            } else {
                hamDocs++;
                for (String t : tokens) {
                    if (TextCleaner.isStopword(t)) continue;
                    hamCounts.put(t, hamCounts.getOrDefault(t, 0) + 1);
                    totalHamTokens++;
                }
            }
        }

        // Build vocab
        Set<String> vocab = new java.util.HashSet<>();
        vocab.addAll(spamCounts.keySet());
        vocab.addAll(hamCounts.keySet());
        vocabSize = vocab.size();

    }

    @Override
    public PredictionResult predictWithScores(String text) {
        Map<String, Double> tfidf = vectorizer.transform(text);

        double priorSpam = Math.log((double) (spamDocs + 1) / (spamDocs + hamDocs + 2));
        double priorHam  = Math.log((double) (hamDocs + 1) / (spamDocs + hamDocs + 2));

        double spamScore = priorSpam;
        double hamScore  = priorHam;

        for (Map.Entry<String, Double> entry : tfidf.entrySet()) {
            String term = entry.getKey();
            double weight = entry.getValue();
            double countSpam = spamCounts.getOrDefault(term, 0);
            double countHam  = hamCounts.getOrDefault(term, 0);

            // Laplace smoothing
            double probTermSpam = (countSpam + 1.0) / (totalSpamTokens + vocabSize + 1.0);
            double probTermHam  = (countHam + 1.0) / (totalHamTokens + vocabSize + 1.0);

            spamScore += weight * Math.log(probTermSpam);
            hamScore  += weight * Math.log(probTermHam);
        }

        String prediction = spamScore > hamScore ? "spam" : "ham";
        return new PredictionResult(prediction, spamScore, hamScore);
    }
}
