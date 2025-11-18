package com.spamshield.api;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.spamshield.ml.NaiveBayesClassifier;
import com.spamshield.models.PredictionResult;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

/**
 * Small REST API server.
 * Provides:
 *  - GET /ping
 *  - POST /classify  { "text": "..." }  -> { prediction, spamScore, hamScore }
 *
 * Note: classifier instance is injected in start(...)
 */
public class ApiServer {

    private static final Gson gson = new Gson();

    /**
     * Start the API server on given port with a classifier instance.
     */
    public static void start(NaiveBayesClassifier classifier, int portNumber) {
        port(portNumber);

        get("/ping", (req, res) -> {
            res.type("application/json");
            return gson.toJson("SpamShield API is running");
        });

        post("/classify", (req, res) -> {
            res.type("application/json");
            try {
                Map<?, ?> body = gson.fromJson(req.body(), Map.class);
                Object t = body.get("text");
                String text = t == null ? "" : t.toString();
                PredictionResult pr = classifier.predictWithScores(text);
                Map<String, Object> out = new HashMap<>();
                out.put("prediction", pr.getPrediction());
                out.put("spamScore", pr.getSpamScore());
                out.put("hamScore", pr.getHamScore());
                return gson.toJson(out);
            } catch (Exception ex) {
                res.status(400);
                return gson.toJson(Map.of("error", "invalid request"));
            }
        });

        System.out.println("API server started on port " + portNumber);
    }
}
