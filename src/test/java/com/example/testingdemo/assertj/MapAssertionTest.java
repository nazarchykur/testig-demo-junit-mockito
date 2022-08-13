package com.example.testingdemo.assertj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Writing assertions for maps")
class MapAssertionTest {

    private static final String INCORRECT_KEY = "incorrectKey";
    private static final String KEY = "key";
    private static final String VALUE = "value";

    private Map<String, String> map;

    @BeforeEach
    void createAndInitializeMap() {
        map = new HashMap<>();
        map.put(KEY, VALUE);
    }


    /*
        If we want to ensure that a map contains the specified key, we have to write our assertion by invoking the containsKey()
     */
    @Test
    @DisplayName("Should contain the correct key")
    void shouldContainCorrectKey() {
        assertThat(map).containsKey(KEY);
    }

    /*
        If we want to ensure that a map doesn't contain the specified key, 
        we have to write our assertion by invoking the doesNotContainKey()
     */
    @Test
    @DisplayName("Should not contain the incorrect key")
    void shouldNotContainIncorrectKey() {
        assertThat(map).doesNotContainKey(INCORRECT_KEY);
    }
    
    /*
        If we want to ensure that a map contains the specified entry, 
        we have to write our assertion by invoking the containsEntry()
     */
    @Test
    @DisplayName("Should contain the given entry")
    void shouldContainGivenEntry() {
        assertThat(map).containsEntry(KEY, VALUE);
    }
    
    /*
        If we want to ensure that a map doesn't contain the specified entry, 
        we have to write our assertion by invoking the doesNotContainEntry()
     */
    @Test
    @DisplayName("Should not contain the given entry")
    void shouldNotContainGivenEntry() {
        assertThat(map).doesNotContainEntry(INCORRECT_KEY, VALUE);
    }

    @Test
    @DisplayName("Chaining assertions to map")
    void chainingAssertions() {
        Map<String, Integer> map = new HashMap<>();
        map.put("leo", 1);
        map.put("don", 2);

        assertThat(map).isNotEmpty()
                .size()
                .isGreaterThan(1)
                .isLessThan(3)
                .returnToMap()
                .containsKeys("leo", "don")
                .containsEntry("leo", 1);
    }

}
