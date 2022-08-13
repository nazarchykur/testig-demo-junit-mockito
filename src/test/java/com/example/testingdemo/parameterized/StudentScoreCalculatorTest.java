package com.example.testingdemo.parameterized;

import com.example.testingdemo.fortestexample.StudentScoreCalculator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StudentScoreCalculatorTest {

    @Test
    public void studentScoreCalculatorRegular() {
        StudentScoreCalculator sCalc = new StudentScoreCalculator();
        sCalc.calculateSatScore(50, 50);

        assertEquals(2500, sCalc.getSatScore());
    }
    
    @Test
    public void studentScoreCalculator_MathsNegative() {
        StudentScoreCalculator sCalc = new StudentScoreCalculator();
        sCalc.calculateSatScore(-1, 50);

        assertEquals(-1, sCalc.getSatScore());
    }

    @Test
    public void studentScoreCalculator_LiteracyNegative() {
        StudentScoreCalculator sCalc = new StudentScoreCalculator();
        sCalc.calculateSatScore(50, -1);

        assertEquals(-1, sCalc.getSatScore());
    }

    @Test
    public void studentScoreCalculator_BothNegative() {
        StudentScoreCalculator sCalc = new StudentScoreCalculator();
        sCalc.calculateSatScore(-1, -1);

        assertEquals(-1, sCalc.getSatScore());
    }

    @Test
    public void studentScoreCalculator_MathsHigh() {
        StudentScoreCalculator sCalc = new StudentScoreCalculator();
        sCalc.calculateSatScore(105, 50);

        assertEquals(-1, sCalc.getSatScore());
    }

    @Test
    public void studentScoreCalculator_LiteracyHigh() {
        StudentScoreCalculator sCalc = new StudentScoreCalculator();
        sCalc.calculateSatScore(50, 150);

        assertEquals(-1, sCalc.getSatScore());
    }

    @Test
    public void studentScoreCalculator_BothHigh() {
        StudentScoreCalculator sCalc = new StudentScoreCalculator();
        sCalc.calculateSatScore(150, 150);

        assertEquals(-1, sCalc.getSatScore());
    }
}