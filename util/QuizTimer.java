package com.quiz.util;

import javax.swing.*;

public class QuizTimer implements Runnable {
    private int timeRemaining; // in seconds
    private JLabel timerLabel;
    private Runnable onFinish;
    private boolean running = true;

    public QuizTimer(int seconds, JLabel timerLabel, Runnable onFinish) {
        this.timeRemaining = seconds;
        this.timerLabel = timerLabel;
        this.onFinish = onFinish;
    }

    @Override
    public void run() {
        while (running && timeRemaining > 0) {
            try {
                Thread.sleep(1000);
                timeRemaining--;
                SwingUtilities.invokeLater(() -> {
                    int min = timeRemaining / 60;
                    int sec = timeRemaining % 60;
                    timerLabel.setText(String.format("Time Left: %02d:%02d", min, sec));
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
        if (running && timeRemaining == 0) {
            SwingUtilities.invokeLater(onFinish);
        }
    }

    public void stop() {
        running = false;
    }
}
