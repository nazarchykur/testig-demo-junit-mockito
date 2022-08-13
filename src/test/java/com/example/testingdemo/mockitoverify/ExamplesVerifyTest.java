package com.example.testingdemo.mockitoverify;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ExamplesVerifyTest {

    @Test
    public void test_verify() {
        List<String> mockList = mock(List.class);

        mockList.add("Leo");
        mockList.size();

        verify(mockList).add("Leo");
    }

    @Test
    public void test_verify_withCountTimes() {
        List<String> mockList = mock(List.class);

        mockList.add("Leo");
        mockList.size();
        
        

        /*
           не можна використовувати  only()  якщо на цьому класі були викликані інші методи або той самий метод кілька разів:
           
                org.mockito.exceptions.verification.NoInteractionsWanted: 
                No interactions wanted here:
         */
//        verify(mockList, only()).add("Leo");

        verify(mockList, times(1)).add("Leo");


        /*
                Mockito verify() method is overloaded, the second one is verify(T mock, VerificationMode mode). 
                We can use it to verify for the invocation count.
         */
        verify(mockList, times(1)).size(); //same as normal verify method
        verify(mockList, atLeastOnce()).size(); // must be called at least once
        verify(mockList, atMost(2)).size(); // must be called at most 2 times
        verify(mockList, atLeast(1)).size(); // must be called at least once
        verify(mockList, never()).clear(); // must never be called
    }

    @Test
    public void test_verify_withCountTimes2() {
        List<String> mockList = mock(List.class);

        mockList.add("Leo");
        mockList.add("Leo");
        mockList.size();

        /*
           не можна використовувати  only()  якщо на цьому класі були викликані інші методи або той самий метод кілька разів:
           
                org.mockito.exceptions.verification.NoInteractionsWanted: 
                No interactions wanted here:
         */
//        verify(mockList, only()).add("Leo");

        verify(mockList, times(2)).add("Leo");
        verify(mockList, times(1)).size();
    }

    @Test
    public void test_verify_donotCheckArguments() {
        List<String> mockList = mock(List.class);

        mockList.add("Leo");
        
        /*
            If we want to make sure a method is called but we don’t care about the argument, 
            then we can use ArgumentMatchers with verify method.
         */

        // ці три варіанти суть одне, просто різними способами:
        verify(mockList).add(anyString());
        verify(mockList).add(any(String.class));
        verify(mockList).add(ArgumentMatchers.any(String.class));
    }

    @Test
    public void test_verify_NoMoreInteractions() {
        List<String> mockList = mock(List.class);

        mockList.add("Leo");

        // all interactions are verified, so below will pass
        verifyNoMoreInteractions(mockList);


        mockList.isEmpty();
        
        /*
            isEmpty() is not verified, so below will fail:
                    org.mockito.exceptions.verification.NoInteractionsWanted: 
                    No interactions wanted here... :
         */
        verifyNoMoreInteractions(mockList);
    }

    @Test
    public void test_verify_only() {
        Map mockMap = mock(Map.class);
        mockMap.isEmpty();
        
        /*
            If we want to verify that only one method is being called, then we can use only() with verify method.
         */
        verify(mockMap, only()).isEmpty();
    }
}
