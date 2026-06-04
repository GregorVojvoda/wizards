package com.gregorv.home.wizards.vo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    // ------------------------
    // ---- Initialization ----
    // ------------------------

    @Test
    void round_initialization() {
        // Given
        Round round = new Round(0);
        // When / Then
        assertEquals(0, round.getPrediction().intValue());
        assertNull(round.getResult());
        assertNull(round.getScore());
    }

    @Test
    void round_initialization_error() {
        // Given / When / Then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Round(-1));
        assertEquals("Prediction cannot be negative", e.getMessage());

    }

    // --------------------
    // ---- Set Result ----
    // --------------------

    @ParameterizedTest
    @CsvSource({
            "0, 0, 20",
            "0, 2, -20",
            "4, 2, -20",
            "2, 2, 40"
    })
    void round_after_result_set(int prediction, int result, int expectedScore) {
        // Given
        Round round = new Round(prediction);
        // When
        round.setResult(result);
        // Then
        assertEquals(expectedScore, round.getScore().intValue());
        assertEquals(result, round.getResult().intValue());
    }

    @Test
    void round_result_set_error() {
        // Given
        Round round = new Round(0);
        // When / Then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> round.setResult(-1));
        assertEquals("Result cannot be negative", e.getMessage());
    }

}
