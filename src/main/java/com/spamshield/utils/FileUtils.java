package com.spamshield.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

    public static List<String> readAllLines(String resourcePath) throws IOException {

        // Load from classpath (works inside JAR too)
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(resourcePath);

        if (is == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.toList());
        }
    }
}
