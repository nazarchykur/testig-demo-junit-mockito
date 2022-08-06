package com.example.testingdemo.mockitoexample;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/*
        Note that the @Spy annotation tries to call the no-args constructor to initialize the mocked object. 
        If your class doesn't have it then you will get the following error:
                    
                    org.mockito.exceptions.base.MockitoException: Unable to initialize @Spy annotated field 'mockUtils'.
                    Please ensure that the type 'Utils' has a no-arg constructor.
	                    ...
	                    
	                    
	    Also, note that Mockito cannot instantiate inner classes, local classes, abstract classes, and interfaces. 
	    So it’s always a good idea to provide an instance to spy on. Otherwise, real methods might not get called 
	    and silently ignored. For example, if you specify a spy object as below:
	    
	                    @Spy
                        List<String> spyList;
        
       You will notice that when you call add() or get() methods, real methods are not getting called. 
       If you specify the spy object like below, then everything will work fine:
                        
                        @Spy
                        List<String> spyList = new ArrayList<>();
	                    
 */

//@RunWith(MockitoJUnitRunner.class) // Junit 4
//@ExtendWith(SpringExtension.class) // works with Junit 5
@ExtendWith(MockitoExtension.class) // works with Junit 5
public class Mockito_SpyAnnotation {

    @Spy
    Utils mockUtils;

    @Test
    public void test() {
        when(mockUtils.process(2, 3)).thenReturn(5);

        //mocked method
        assertEquals(5, mockUtils.process(2, 3));

        //real method called since it's not stubbed
        assertEquals(10, mockUtils.process(2, 8));

    }
}

class Utils {
    
    // for example if we do not have a default constructor
    
//    private int a;
//
//    public Utils(int a) {
//        this.a = a;
//    }
    
    public int process(int x, int y) {
        System.out.println("Input Params = " + x + "," + y);
        return x + y;
    }
}