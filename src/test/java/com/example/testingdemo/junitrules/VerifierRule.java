package com.example.testingdemo.junitrules;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.Verifier;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

/*
    Verifier Rule
    
     The verifier rule does a verification check and if it is failed, the test is finished with a failing result. 
     You can write your custom verification logic with Verifier Rule.
 */

public class VerifierRule {
    private List<String> errorLog = new ArrayList<>();
    
    @Rule
    public Verifier verifier = new Verifier() {
        //After each method perform this check.
        @Override public void verify() {
            assertTrue("Error Log is not Empty!", errorLog.isEmpty());
        }
    };
    
    @Test
    public void testWritesErrorLog() {
        //...
        errorLog.add("There is an error!"); // failed and we will see error log described in Rule
    }
}


/*

    https://www.swtestacademy.com/junit-rules/
    
    JUnit Rules allow you to write code to do some before and after work. 
    Thus, you don’t repeat to write the same code in various test classes. 
    They are very useful to add more functionalities to all test methods in a test class. 
    You can extend or reuse provided rules or write your own custom rules.
    
    The base rules are listed below and you can find details about them on https://github.com/junit-team/junit/wiki/Rules web page.
    
        TemporaryFolder Rule
        ExternalResource Rules
        ErrorCollector Rule
        Verifier Rule
        TestWatchman/TestWatcher Rules
        TestName Rule
        Timeout Rule
        ExpectedException Rules
        ClassRule
        RuleChain
        Custom Rules
    
    In JUnit https://github.com/junit-team/junit4/wiki/Rules link, you can find all details of each rule. 
    Also, their core mechanism and flow are described in this article very well. I want to demonstrate the usage of some rules with examples.
 */