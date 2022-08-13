package com.example.testingdemo.parameterized;

import com.example.testingdemo.fortestexample.StudentScoreCalculator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StudentScoreCalculatorTest_usingBefore {
    StudentScoreCalculator sCalc;  
    
    @Before
    public void setUp() {
        sCalc = new StudentScoreCalculator();
    }

    @Test
    public void studentScoreCalculatorRegular() {
        sCalc.calculateSatScore(50, 50);

        assertEquals(2500, sCalc.getSatScore());
    }
    
    @Test
    public void studentScoreCalculator_MathsNegative() {
        sCalc.calculateSatScore(-1, 50);

        assertEquals(-1, sCalc.getSatScore());
    }

    @Test
    public void studentScoreCalculator_LiteracyNegative() {
        sCalc.calculateSatScore(50, -1);

        assertEquals(-1, sCalc.getSatScore());
    }

    @Test
    public void studentScoreCalculator_BothNegative() {
        sCalc.calculateSatScore(-1, -1);

        assertEquals(-1, sCalc.getSatScore());
    }

    @Test
    public void studentScoreCalculator_MathsHigh() {
        sCalc.calculateSatScore(105, 50);

        assertEquals(-1, sCalc.getSatScore());
    }

    @Test
    public void studentScoreCalculator_LiteracyHigh() {
        sCalc.calculateSatScore(50, 150);

        assertEquals(-1, sCalc.getSatScore());
    }

    @Test
    public void studentScoreCalculator_BothHigh() {
        sCalc.calculateSatScore(150, 150);

        assertEquals(-1, sCalc.getSatScore());
    }
}