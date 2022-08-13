package com.example.testingdemo.assertj;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/*
Two arrays are considered as equal if:
    -They are both null or empty.
    
    -Both arrays contain the "same" objects or values. To be more specific, 
    JUnit 5 iterates both arrays one element at a time and ensures that the elements found from the given index are equal.
 */

@DisplayName("Write assertions for arrays")
class ArrayAssertionTest {

    @Nested
    @DisplayName("When two arrays are equal")
    class WhenArraysAreEqual {

        @Nested
        @DisplayName("When arrays contain integers")
        class WhenArraysContainIntegers {

            final int[] ACTUAL = new int[]{2, 5, 7};
            final int[] EXPECTED = new int[]{2, 5, 7};

            @Test
            @DisplayName("Should contain the same integers")
            void shouldContainSameIntegers() {
                assertThat(ACTUAL).isEqualTo(EXPECTED);
            }
        }
    }

    @Nested
    @DisplayName("When arrays contain integers")
    class WhenArraysContainIntegers {

        final int[] ACTUAL = new int[]{2, 6, 7};
        final int[] EXPECTED = new int[]{2, 5, 7};

        @Test
        @DisplayName("Should not contain the same integers")
        void shouldNotContainSameIntegers() {
            assertThat(ACTUAL).isNotEqualTo(EXPECTED);
        }
    }
}
