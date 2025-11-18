package com.spamshield.ml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.spamshield.models.PredictionResult;
import com.spamshield.preprocessing.Tokenizer;
import com.spamshield.utils.TextCleaner;

/**
 * Multinomial Naive Bayes classifier using TF-IDF for weighting terms at prediction.
 */
public class NaiveBayesClassifier extends Classifier {

    private final Tokenizer tokenizer;
    private final TFIDFVectorizer vectorizer;

    private final Map<String, Integer> spamCounts = new HashMap<>();
    private final Map<String, Integer> hamCounts = new HashMap<>();
    private int totalSpamTokens = 0;
    private int totalHamTokens = 0;
    private int spamDocs = 0;
    private int hamDocs = 0;
    private int vocabSize = 0;
    private boolean trained = false;
    private final double alpha = 1.0;

    public NaiveBayesClassifier(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        this.vectorizer = new TFIDFVectorizer(tokenizer);
    }

    @Override
    public synchronized void train(String datasetPath) {
        try {
            List<com.spamshield.models.Email> data = EmailDatasetLoader.load(datasetPath);

            List<String> docs = new ArrayList<>();
            for (com.spamshield.models.Email e : data) docs.add(e.getText());
            vectorizer.fit(docs);

            spamCounts.clear();
            hamCounts.clear();
            totalSpamTokens = 0;
            totalHamTokens = 0;
            spamDocs = 0;
            hamDocs = 0;

            for (com.spamshield.models.Email e : data) {
                String label = e.getLabel();
                List<String> tokens = tokenizer.tokenize(TextCleaner.clean(e.getText()));
                for (String t : tokens) {
                    if (t.isBlank()) continue;
                    if (TextCleaner.isStopword(t)) continue;
                    if (label.equals("spam")) {
                        spamCounts.put(t, spamCounts.getOrDefault(t, 0) + 1);
                        totalSpamTokens++;
                    } else {
                        hamCounts.put(t, hamCounts.getOrDefault(t, 0) + 1);
                        totalHamTokens++;
                    }
                }
                if (label.equals("spam")) spamDocs++; else hamDocs++;
            }

            Set<String> vocab = new HashSet<>();
            vocab.addAll(spamCounts.keySet());
            vocab.addAll(hamCounts.keySet());
            vocab.addAll(vectorizer.getVocabulary());
            vocabSize = vocab.size();
            trained = true;
            System.out.printf("Training finished. docs=%d (spam=%d ham=%d) vocab=%d%n",
                    spamDocs + hamDocs, spamDocs, hamDocs, vocabSize);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public synchronized PredictionResult predictWithScores(String text) {
        if (!trained) {
            return new PredictionResult("UNKNOWN", 0.0, 0.0);
        }
        Map<String, Double> tfidf = vectorizer.transform(text);

        double priorSpam = Math.log(((double) spamDocs + 1.0) / (spamDocs + hamDocs + 2.0));
        double priorHam = Math.log(((double) hamDocs + 1.0) / (spamDocs + hamDocs + 2.0));

        double spamScore = priorSpam;
        double hamScore = priorHam;

        for (Map.Entry<String, Double> e : tfidf.entrySet()) {
            String term = e.getKey();
            double weight = e.getValue();
            if (weight == 0) continue;

            double spamCount = spamCounts.getOrDefault(term, 0);
            double hamCount = hamCounts.getOrDefault(term, 0);

            double pTermGivenSpam = (spamCount + alpha) / (totalSpamTokens + alpha * Math.max(1, vocabSize));
            double pTermGivenHam = (hamCount + alpha) / (totalHamTokens + alpha * Math.max(1, vocabSize));

            spamScore += weight * Math.log(pTermGivenSpam);
            hamScore += weight * Math.log(pTermGivenHam);
        }

        String pred = spamScore > hamScore ? "spam" : "ham";
        return new PredictionResult(pred, spamScore, hamScore);
    }
}
