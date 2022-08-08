package com.example.testingdemo.junitrules;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.concurrent.TimeUnit;

/*
    Timeout Rule
    
    The Timeout Rule applies the same timeout to all test methods in a class.
 */

public class TimeoutRule {
    @Rule
    public Timeout timeout = new Timeout(2, TimeUnit.SECONDS); // global timeout for all tests

    /*
        this testA passed because timeout is 2 seconds 
        and here we added 1 seconds of pause.
     */
    @Test
    public void testA() throws Exception {
        Thread.sleep(1000); // passed
    }

    /*
        this testB fails because timeout is 2 seconds 
        and here we added 3 seconds of pause.
        
        org.junit.runners.model.TestTimedOutException: test timed out after 2 seconds
     */
    @Test
    public void testB() throws Exception {
        Thread.sleep(3000); // fails
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