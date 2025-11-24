package com.spamshield.api;

import com.google.gson.Gson;
import com.spamshield.ml.NaiveBayesClassifier;
import com.spamshield.models.PredictionResult;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class ApiServer {

    public static void start(NaiveBayesClassifier nb) {

        port(4567);
        staticFiles.location("public");

        // Simple health endpoint
        get("/ping", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson("pong");
        });

        // POST /classify { "email": "text here" }
        post("/classify", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();

            EmailRequest body = gson.fromJson(req.body(), EmailRequest.class);

            if (body == null || body.email == null || body.email.isBlank()) {
                res.status(400);
                return gson.toJson(new ErrorResponse("Missing or empty 'email' field"));
            }

            // predictWithScores gives both label + scores
            PredictionResult result = nb.predictWithScores(body.email);
            String label = result.getPrediction();

            // Optional: print for debugging
            System.out.println("Classified: '" + body.email + "' => " + label +
                    " (spamScore=" + result.getSpamScore() + ", hamScore=" + result.getHamScore() + ")");

            return gson.toJson(new PredictionResponse(label, result.getSpamScore(), result.getHamScore()));
        });
    }

    static class EmailRequest {
        String email;
    }

    static class PredictionResponse {
        String result;
        double spamScore;
        double hamScore;
        PredictionResponse(String r, double s, double h) { this.result = r; this.spamScore = s; this.hamScore = h; }
    }

    static class ErrorResponse {
        String error;
        ErrorResponse(String e) { this.error = e; }
    }
}
