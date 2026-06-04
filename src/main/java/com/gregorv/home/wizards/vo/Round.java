package com.gregorv.home.wizards.vo;

import java.math.BigInteger;

public class Round {

    private static final int BASE_WINNING_BONUS = 20;

    private final BigInteger prediction;
    private BigInteger result;
    private BigInteger score;

    public Round(int p) {
        if (p < 0) {
            throw new IllegalArgumentException("Prediction cannot be negative");
        }
        this.prediction = BigInteger.valueOf(p);
    }

    public void setResult(int r) {
        if (r < 0) {
            throw new IllegalArgumentException("Result cannot be negative");
        }

        this.result = BigInteger.valueOf(r);
        setScore();
    }

    public BigInteger getPrediction() {
        return prediction;
    }

    public BigInteger getResult() {
        return result;
    }

    public BigInteger getScore() {
        return score;
    }

    private void setScore() {
        if (prediction.equals(result)) {
            score = BigInteger.valueOf(BASE_WINNING_BONUS).add(prediction.multiply(BigInteger.TEN));
        } else if (prediction.compareTo(result) > 0) {
            score = result.subtract(prediction).multiply(BigInteger.TEN);
        } else {
            score = prediction.subtract(result).multiply(BigInteger.TEN);
        }
    }
}
