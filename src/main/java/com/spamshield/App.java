package com.spamshield;

import com.spamshield.api.ApiServer;

public class App {
    public static void main(String[] args) {

        System.out.println("Starting SpamShield...");

        // Start REST API server
        ApiServer.start();

    }
}
