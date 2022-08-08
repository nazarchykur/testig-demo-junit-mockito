package com.example.testingdemo.junitrules;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/*
    Error Collector Rule
    
    The ErrorCollector Rule allows execution of a test to continue after the first problem is found. 
    It collects all the errors and reports them all at once.
 */

public class ErrorCollectorRule {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void example() {
        collector.addError(new Throwable("First Error!"));
        collector.addError(new Throwable("Second Error!"));
        
        collector.checkThat(5, is(8)); //First Error
        collector.checkThat(5, is(not(8))); //Passed
        collector.checkThat(5, is(equalTo(9))); //Second Error
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