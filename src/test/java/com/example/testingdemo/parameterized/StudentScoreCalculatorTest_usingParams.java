package com.example.testingdemo.parameterized;

import com.example.testingdemo.fortestexample.StudentScoreCalculator;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/*
    need to add this dependency to use parameterized test:
    
    	<dependency>
			<groupId>pl.pragmatists</groupId>
			<artifactId>JUnitParams</artifactId>
			<version>1.1.1</version>
			<scope>test</scope>
		</dependency>
 */

@RunWith(JUnitParamsRunner.class)
public class StudentScoreCalculatorTest_usingParams {

    private static Object[] testValues() {
        return new Object[]{
                new Object[]{50, 50, 2500},
                new Object[]{-10, 50, -1},
                new Object[]{10, -50, -1},
                new Object[]{-1, -1, -1},
                new Object[]{110, 50, -1},
                new Object[]{10, 150, -1},
                new Object[]{150, 150, -1}
        };
    }
    
    @Test
    @Parameters(method = "testValues")
    public void studentScoreCalculator(int mathsScore, int literacyScore, int expectedScore) {
        StudentScoreCalculator sCalc = new StudentScoreCalculator();

        sCalc.calculateSatScore(mathsScore, literacyScore);

        assertEquals(expectedScore, sCalc.getSatScore());
    }

}