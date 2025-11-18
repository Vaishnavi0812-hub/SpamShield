package com.spamshield.api;


import java.util.Map;

import com.google.gson.Gson;
import com.spamshield.ml.NaiveBayesClassifier;
import com.spamshield.models.PredictionResult;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class ApiServer {
    private static final Gson gson = new Gson();
    private static NaiveBayesClassifier classifier;

    public static void start(NaiveBayesClassifier nb) {
        classifier = nb;
        port(4567);

        get("/", (req, res) -> {
        res.type("text/html");
        return "<h1>Welcome to SpamShield API</h1><p>Use /ping or /classify</p>";
        });


        post("/classify", (req, res) -> {
            res.type("application/json");
            // expecting JSON: { "text": "email body here" }
            Map<?, ?> body = gson.fromJson(req.body(), Map.class);
            String text = (String) body.get("text");
            PredictionResult pr = classifier.predictWithScores(text);
            return gson.toJson(pr);
        });

        System.out.println("API started at http://localhost:4567");
    }
}
