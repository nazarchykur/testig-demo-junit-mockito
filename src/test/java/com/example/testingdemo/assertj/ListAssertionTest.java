package com.example.testingdemo.assertj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*
    - If we want to verify that an iterable is empty, we can write our assertion by invoking the isEmpty() 
    - If we want to ensure that an iterable isn't empty, we can write our assertion by invoking the isNotEmpty()
    - If we want to verify that the size of the iterable is correct, we can write our assertion by invoking the hasSize()
 */

@DisplayName("Writing assertions for lists")
class ListAssertionTest {

    @Nested
    @DisplayName("When we write assertions for elements")
    class WhenWeWriteAssertionsForElements {

        private Object first;
        private Object second;
        private List<Object> list;

        @BeforeEach
        void createAndInitializeList() {
            first = new Object();
            second = new Object();

            list = Arrays.asList(first, second);
        }

        @Test
        @DisplayName("Should contain two elements")
        void shouldContainTwoElements() {
//            assertThat(list).isNotEmpty();
//            assertThat(list).hasSize(2);

            assertThat(list).isNotEmpty().hasSize(2);
        }

        /*
            If we want to ensure that an iterable contains only the expected elements in the given order, 
            we have to write our assertion by using the containsExactly()
         */
        @Test
        @DisplayName("Should contain the correct elements in the given order")
        void shouldContainCorrectElementsInGivenOrder() {
            assertThat(list).containsExactly(first, second);
        }
        
        /*
            If we want to verify that an iterable contains only the expected elements in any order, we have
            to write our assertion by using the containsExactlyInAnyOrder()
         */
        @Test
        @DisplayName("Should contain the correct elements in any order")
        void shouldContainCorrectElementsInAnyOrder() {
            assertThat(list).containsExactlyInAnyOrder(second, first);
        }
        
        /*
            If we want to ensure that an iterable contains the specified element, we have
            to write our assertion by using the containsOnlyOnce()
         */
        @Test
        @DisplayName("Should contain the correct element once")
        void shouldContainCorrectElementOnce() {
            assertThat(list).containsOnlyOnce(first);
        }
        
        /*
            If we want to ensure that an iterable doesn't contain the specified element, we have
            to write our assertion by using the doesNotContain()
         */
        @Test
        @DisplayName("Should not contain an incorrect element")
        void shouldNotContainIncorrectElement() {
            assertThat(list).doesNotContain(new Object());
        }
    }

    /*
        If we want to verify that two iterables are deeply equal, we have to to write our assertion by using the isEqualTo()
     */
    @Nested
    @DisplayName("When we compare two lists")
    class WhenWeCompareTwoLists {

        private final List<Integer> FIRST = Arrays.asList(1, 2, 3);
        private final List<Integer> SECOND = Arrays.asList(1, 2, 3);

        @Test
        @DisplayName("Should contain the same elements")
        void shouldContainSameElements() {
            assertThat(FIRST).isEqualTo(SECOND);
        }
    }

 
}
