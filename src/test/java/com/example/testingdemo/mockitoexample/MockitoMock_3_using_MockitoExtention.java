package com.example.testingdemo.mockitoexample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class) // Junit 4
//@ExtendWith(SpringExtension.class) // works with Junit 5
@ExtendWith(MockitoExtension.class) // works with Junit 5
public class MockitoMock_3_using_MockitoExtention {
    @Mock
    List<String> mockList;
    

    @Test
    public void test() {
        when(mockList.get(0)).thenReturn("item 1");
        
        assertEquals("item 1", mockList.get(0));
    }
}
