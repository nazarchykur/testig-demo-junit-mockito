package com.example.testingdemo.fortestexample;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
    Mockito ArgumentCaptor is used to capture arguments for mocked methods. 
    ArgumentCaptor is used with Mockito verify() methods to get the arguments passed when any method is called. 
    This way, we can provide additional JUnit assertions for our tests.
        
        
    Mockito ArgumentCaptor
        
        We can create ArgumentCaptor instance for any class, then its capture() method is used with verify() methods. 
        Finally, we can get the captured arguments from getValue() and getAllValues() methods. 
        
            getValue() method can be used when we have captured a single argument. 
            If the verified method was called multiple times then getValue() method will return the latest captured value. 
        
            If multiple arguments are captured, call getAllValues() to get the list of arguments.

        
 */

@ExtendWith(MockitoExtension.class)
class MathUtilsTest_Mockito_ArgumentCaptor {

    @Mock
    private MathUtils mockMathUtils;

    @Test
    void test() {
        when(mockMathUtils.add(1, 2)).thenReturn(3);
        when(mockMathUtils.isInteger(anyString())).thenReturn(true);

        given(mockMathUtils.add(1, 2)).willReturn(3);

        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        assertEquals(3, mockMathUtils.add(1, 2));
        
        assertTrue(mockMathUtils.isInteger("1"));
        assertTrue(mockMathUtils.isInteger("2"));

        verify(mockMathUtils).add(integerArgumentCaptor.capture(), integerArgumentCaptor.capture());

        List<Integer> allValues = integerArgumentCaptor.getAllValues();

        assertEquals(Arrays.asList(1, 2), allValues);

    }

}