package com.example.testingdemo.fortestexample;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    
    Mockito @Captor
        
        We can use @Captor annotation to create argument captor at field level. So instead of initializing field level ArgumentCaptor as:
                ArgumentCaptor acLong = ArgumentCaptor.forClass(Long.class);
                
        We can use @Captor as:
                @Captor ArgumentCaptor acLong;
 */

@ExtendWith(MockitoExtension.class)
class MathUtilsTest_Mockito_CaptorAnnotation {

    @Mock
    private MathUtils mockMathUtils;

    //    ArgumentCaptor<Long> acLong = ArgumentCaptor.forClass(Long.class);
    @Captor
    ArgumentCaptor<Long> acLong;


    @Test
    void test() {
        when(mockMathUtils.squareLong(2L)).thenReturn(4L);
        
        assertEquals(4L, mockMathUtils.squareLong(2L));
        
        verify(mockMathUtils).squareLong(acLong.capture());

        assertEquals(2, acLong.getValue());
    }

}