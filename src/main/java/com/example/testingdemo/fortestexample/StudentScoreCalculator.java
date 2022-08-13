package com.example.testingdemo.fortestexample;

public class StudentScoreCalculator {
    private int satScore;
    
    public void calculateSatScore(int mathsScore, int literacyScore) {
        if (mathsScore < 0 || mathsScore > 100 || literacyScore < 0 || literacyScore > 100) {
            satScore = -1;
        } else {
            satScore = mathsScore * literacyScore;
        }
    }

    public int getSatScore() {
        return satScore;
    }

    private boolean isBetween(int value) {
        return value < 0 || value > 100;
    }
}
