package com.spamshield.ml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.spamshield.models.Email;
import com.spamshield.utils.FileUtils;

public class EmailDatasetLoader {

    /**
     * Reads CSV file with format: label,text
     * label must be 'spam' or 'ham'
     */
    public static List<Email> load(String path) throws IOException {
        List<String> lines = FileUtils.readAllLines(path);
        List<Email> out = new ArrayList<>();
        for (String line : lines) {
            if (line == null || line.isBlank()) continue;
            int idx = line.indexOf(',');
            if (idx == -1) continue;
            String label = line.substring(0, idx).trim().toLowerCase();
            String text = line.substring(idx + 1).trim();
            if (!label.equals("spam") && !label.equals("ham")) continue;
            out.add(new Email(label, text));
        }
        return out;
    }
}
