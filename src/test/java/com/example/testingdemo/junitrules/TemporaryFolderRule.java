package com.example.testingdemo.junitrules;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/*
    Temporary Folder Rule
    
    The TemporaryFolder Rule allows you to create files and folders. 
    These files are folders that are deleted whether the test passes or fails when the test method finishes. 
    By default, no exception is thrown if resources cannot be deleted. So, if you need to run a test that needs 
    a temporary file or folder then you can use this rule as shown below.

 */


public class TemporaryFolderRule {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testFile() throws IOException {
        File newFolder = tempFolder.newFolder("newFolder");
        File testFile = tempFolder.newFile("test.txt");

        assertTrue(newFolder.exists());
        assertTrue(testFile.exists());

        //Do something else...
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