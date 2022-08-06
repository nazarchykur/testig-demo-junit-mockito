package com.example.testingdemo.mockitoexample;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


/*
        If we want to mock only specific behaviors and call the real methods for unstubbed behaviors, 
        then we can create a spy object using Mockito spy() method.
 */

public class Mockito_SpyMethod {
    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        List<String> spyOnList = spy(list);

        when(spyOnList.size()).thenReturn(10);
        assertEquals(10, spyOnList.size());

        //calling real methods since below methods are not stubbed
        spyOnList.add("leo");
        spyOnList.add("don");
        assertEquals("leo", spyOnList.get(0));
        assertEquals("don", spyOnList.get(1));
    }
    
}
