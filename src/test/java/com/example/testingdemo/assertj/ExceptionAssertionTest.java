package com.example.testingdemo.assertj;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

/*
    If we want to write assertions for the exception thrown by the system under test, we can use one of these two options:

    First, we can use the static assertThatThrownBy() method of the org.assertj.core.api.Assertions class. 
    When we use this method, we have to know these two things:
    
       - It takes a ThrowingCallable object as a method parameter. This object invokes the system under test.
       - It returns an AbstractThrowableAssert object. We have to use this object when we write assertions for 
            the exception thrown by the system under test.
            
            
     Second, we can catch the thrown exception by using the static catchThrowable() method of 
     the org.assertj.core.api.Assertions class. This method can take two method parameters which are described in the following:

        - A ThrowingCallable object that invokes the system under test.
        - A Class object that specifies the type of the expected exception. This is an optional parameter, 
        and if we pass it to the catchThrowable() method, it specifies the type of the returned exception object. 
        If we omit this method parameter, the catchThrowable() method returns a Throwable object.
               

 */
@DisplayName("Writing assertions for exceptions")
class ExceptionAssertionTest {

    @Nested
    @DisplayName("When we write assertions directly to the thrown exception")
    class WhenWeWriteAssertionsForThrownException {

        @Nested
        @DisplayName("When the system under test throws the correct exception")
        class WhenSystemUnderTestThrowsException {

            /*
                If we want to verify that the system under test throws the expected exception, 
                we have to write our assertion by using the isExactlyInstanceOf()
             */
            @Test
            @DisplayName("Should throw the correct exception")
            void shouldThrowCorrectException() {
                assertThatThrownBy(() -> { throw new NullPointerException(); })
                        .isExactlyInstanceOf(NullPointerException.class);
            }
            
            /*
                If we want to verify that the system under test throws an exception that has the expected message, 
                we have to write our assertion by using the hasMessage()
             */
            @Test
            @DisplayName("Should throw an exception that has the correct message")
            void shouldThrowAnExceptionWithCorrectMessage() {
                assertThatThrownBy(() -> {
                    throw new NullPointerException("NPE error");
                }).hasMessage("NPE error");
            }

            @Test
            @DisplayName("Should throw the correct exception")
            void shouldThrowCorrectException_using_catchThrowable() {
                final Throwable thrown = catchThrowable(() -> {
                    throw new NullPointerException();
                });
                
                assertThat(thrown).isExactlyInstanceOf(NullPointerException.class);
            }

            @Test
            @DisplayName("Should throw an exception that has the correct message")
            void shouldThrowAnExceptionWithCorrectMessage_using_catchThrowable() {
                final Throwable thrown = catchThrowable(() -> {
                    throw new NullPointerException("NPE error"); }
                );
                assertThat(thrown.getMessage()).isEqualTo("NPE error");
            }
        }
    }
}
