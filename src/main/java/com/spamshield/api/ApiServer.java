package com.spamshield.api;

import com.google.gson.Gson;

import static spark.Spark.get;
import static spark.Spark.port;

public class ApiServer {

    private static final Gson gson = new Gson();

    public static void start() {

        port(4567); // your API will run on localhost:4567

        get("/ping", (req, res) -> {
            res.type("application/json");
            return gson.toJson("SpamShield API is running!");
        });

        System.out.println("API started at http://localhost:4567/ping");
    }
}
