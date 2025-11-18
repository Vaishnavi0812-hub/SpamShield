package com.spamshield.ml;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.spamshield.preprocessing.Tokenizer;
import com.spamshield.utils.TextCleaner;

public class TFIDFVectorizer {

    private final Tokenizer tokenizer;
    private final Map<String, Double> idf = new HashMap<>();
    private final Set<String> vocabulary = new HashSet<>();
    private boolean fitted = false;

    public TFIDFVectorizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public void fit(Collection<String> documents) {
        Map<String, Integer> docFreq = new HashMap<>();
        int N = 0;
        for (String doc : documents) {
            N++;
            Set<String> seen = new HashSet<>();
            List<String> tokens = tokenizer.tokenize(TextCleaner.clean(doc));
            for (String t : tokens) {
                if (t.isBlank()) continue;
                if (TextCleaner.isStopword(t)) continue;
                seen.add(t);
            }
            for (String term : seen) {
                docFreq.put(term, docFreq.getOrDefault(term, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : docFreq.entrySet()) {
            String term = entry.getKey();
            double df = entry.getValue();
            double idfValue = Math.log((double) N / (1 + df));
            idf.put(term, idfValue);
            vocabulary.add(term);
        }

        fitted = true;
    }

    public Map<String, Double> transform(String document) {
        List<String> tokens = tokenizer.tokenize(TextCleaner.clean(document));
        Map<String, Integer> tf = new HashMap<>();
        int total = 0;
        for (String t : tokens) {
            if (t.isBlank()) continue;
            if (TextCleaner.isStopword(t)) continue;
            tf.put(t, tf.getOrDefault(t, 0) + 1);
            total++;
        }

        Map<String, Double> tfidf = new HashMap<>();
        if (total == 0) return tfidf;

        for (Map.Entry<String, Integer> entry : tf.entrySet()) {
            String term = entry.getKey();
            double termTf = (double) entry.getValue() / total;
            double termIdf = idf.getOrDefault(term, Math.log(documentsCountFallback()));
            tfidf.put(term, termTf * termIdf);
        }

        return tfidf;
    }

    private double documentsCountFallback() {
        // fallback idf for unseen term
        return Math.log(1 + 1.0);
    }

    public Set<String> getVocabulary() {
        return Collections.unmodifiableSet(vocabulary);
    }
}
