package com.spamshield.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.spamshield.ml.NaiveBayesClassifier;
import com.spamshield.models.PredictionResult;

/**
 * Simple Swing UI for quick testing.
 */
public class MainUI {

    private final NaiveBayesClassifier classifier;
    private final JFrame frame;
    private final JTextArea inputArea;
    private final JLabel resultLabel;
    private final JLabel statusLabel;

    public MainUI(NaiveBayesClassifier classifier) {
        this.classifier = classifier;
        frame = new JFrame("SpamShield");
        inputArea = new JTextArea(8, 50);
        resultLabel = new JLabel("Prediction: - ");
        statusLabel = new JLabel("Model: training...");
        build();
    }

    private void build() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout(8,8));

        JPanel top = new JPanel(new BorderLayout());
        top.add(statusLabel, BorderLayout.WEST);
        JButton refresh = new JButton("Refresh Status");
        refresh.addActionListener(e -> updateStatus());
        top.add(refresh, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);

        panel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton classify = new JButton("Classify Email");
        classify.addActionListener(this::onClassify);
        bottom.add(classify);
        JButton clear = new JButton("Clear");
        clear.addActionListener(e -> inputArea.setText(""));
        bottom.add(clear);
        bottom.add(resultLabel);

        panel.add(bottom, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private void updateStatus() {
        statusLabel.setText("Model: " + (classifier != null ? "Loaded/ready if training finished" : "No classifier"));
    }

    private void onClassify(ActionEvent ev) {
        String text = inputArea.getText();
        if (text == null || text.isBlank()) {
            JOptionPane.showMessageDialog(frame, "Please paste the email text to classify.");
            return;
        }
        PredictionResult pr = classifier.predictWithScores(text);
        String res = String.format("Prediction: %s (spam=%.4f ham=%.4f)",
                pr.getPrediction().toUpperCase(), pr.getSpamScore(), pr.getHamScore());
        resultLabel.setText(res);
    }

    public void show() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}
