package com.example.testingdemo.mockitoexample;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class) // Junit 4
//@ExtendWith(SpringExtension.class) // works with Junit 5
@ExtendWith(MockitoExtension.class) // works with Junit 5
public class MockitoMock_with_injectMock {
    @Mock
    List<String> mockList;
    
    //@InjectMock creates an instance of the class and 
    //injects the mocks that are marked with the annotations @Mock into it.
    @InjectMocks
    Fruits mockFruits;

    @Test
    public void test() {
        when(mockList.get(0)).thenReturn("Apple");
        when(mockList.size()).thenReturn(1);
        assertEquals("Apple", mockList.get(0));
        assertEquals(1, mockList.size());

        //mockFruits names is using mockList, below asserts confirm it
        assertEquals("Apple", mockFruits.getNames().get(0));
        assertEquals(1, mockFruits.getNames().size());

        mockList.add(1, "Mango");
        // because mockList.get(1) is not stubbed
        assertNull(mockList.get(1));
    }
}

class Fruits{
    private List<String> names;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

}