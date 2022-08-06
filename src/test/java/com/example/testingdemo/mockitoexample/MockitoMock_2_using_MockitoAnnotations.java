package com.example.testingdemo.mockitoexample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MockitoMock_2_using_MockitoAnnotations {
    @Mock
    List<String> mockList;

    @BeforeEach
    public void setup() {
        //if we don't call below, we will get NullPointerException
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test() {
        when(mockList.get(0)).thenReturn("item 1");
        
        assertEquals("item 1", mockList.get(0));
    }
}
