package com.example.testingdemo.assertj;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Write assertions for booleans")
class BooleanAssertionTest {

    @Nested
    @DisplayName("When boolean is true/false")
    class WhenBooleanIsTrueFalse {

        @Test
        @DisplayName("Should be true")
        void shouldBeTrue() {
            assertThat(true).isTrue();
        }

        @Test
        @DisplayName("Should be false")
        void shouldBeFalse() {
            assertThat(false).isFalse();
        }
    }
}
