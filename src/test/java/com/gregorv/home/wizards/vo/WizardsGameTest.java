package com.gregorv.home.wizards.vo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WizardsGameTest {

    // ------------------------
    // ---- Initialization ----
    // ------------------------

    @Test
    void game_initialization_OK() {
        // Given / When
        WizardsGame game = new WizardsGame("Test Game", Set.of("Alice", "Bob", "Charlie"));

        // Then
        assertEquals("Test Game", game.getName());
        assertEquals(3, game.getAliases().size());
        assertEquals(1, game.getRound());
        assertEquals(3, game.getScore().size());
        assertEquals(BigInteger.ZERO, game.getScore().get("Alice"));
        assertEquals(BigInteger.ZERO, game.getScore().get("Bob"));
        assertEquals(BigInteger.ZERO, game.getScore().get("Charlie"));
        assertEquals(20, game.gameNumberOfRounds());
    }

    @Test
    void game_initialization_tooManyAliases() {
        // Given / When / Then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new WizardsGame("Test Game", Set.of("Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace")));

        assertEquals("At most 6 aliases are required", e.getMessage());
    }

    @Test
    void game_initialization_notEnoughAliases() {
        // Given / When / Then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new WizardsGame("Test Game", Set.of("Alice")));

        assertEquals("At least 2 aliases are required", e.getMessage());
    }

    @Test
    void game_initialization_notEnoughAliases_empySet() {
        // Given / When / Then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new WizardsGame("Test Game", Set.of()));

        assertEquals("At least 2 aliases are required", e.getMessage());
    }

    // ------------------------------
    // ---- Get Number Of Rounds ----
    // ------------------------------

    @ParameterizedTest
    @MethodSource("provideAliasesForNumberOfRounds")
    void game_getNumberOfRounds(Set<String> aliases, int expectedNumberOfRounds) {
        // Given
        WizardsGame game = new WizardsGame("Test Game", aliases);

        // When / Then
        assertEquals(expectedNumberOfRounds, game.gameNumberOfRounds());
    }

    static Stream<Arguments> provideAliasesForNumberOfRounds() {
        return Stream.of(
                Arguments.of(Set.of("Alice", "Bob"), 20),
                Arguments.of(Set.of("Alice", "Bob", "Charlie"), 20),
                Arguments.of(Set.of("Alice", "Bob", "Charlie", "David"), 15),
                Arguments.of(Set.of("Alice", "Bob", "Charlie", "David", "Eve"), 12),
                Arguments.of(Set.of("Alice", "Bob", "Charlie", "David", "Eve", "Frank"), 10)
        );
    }


    // -------------------------
    // ---- Set Predictions ----
    // -------------------------

    @Test
    void game_setPredictions_settingDoublePredictions() {
        // Given
        WizardsGame game = new WizardsGame("Test Game", Set.of("Bob", "Charlie"));

        game.setRoundPredictions(Map.of(
                "Bob", 1,
                "Charlie", 0
        ));

        // When / Then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> game.setRoundPredictions(Map.of(
                "Bob", 1,
                "Charlie", 0
        )));

        assertEquals("Predictions for round 1 have already been set", e.getMessage());
    }

    @Test
    void game_setPredictions_SettingTooMuchActors() {
        // Given
        WizardsGame game = new WizardsGame("Test Game", Set.of("Bob", "Charlie"));

        // When / Then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> game.setRoundPredictions(Map.of(
                "Bob", 1,
                "Charlie", 0,
                "Margaret", 0
        )));

        assertEquals("Unknown alias: Margaret", e.getMessage());
    }

    @Test
    void game_setPredictions_notSettingAllActors() {
        // Given
        WizardsGame game = new WizardsGame("Test Game", Set.of("Bob", "Charlie"));

        // When


        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> game.setRoundPredictions(Map.of(
                "Bob", 1
        )));

        assertEquals("Predictions must be provided for all aliases", e.getMessage());
    }

    @Test
    void game_setPredictions_predictionTooHighForRound() {
        // Given
        WizardsGame game = new WizardsGame("Test Game", Set.of("Bob", "Charlie"));


        // When / Then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> game.setRoundPredictions(Map.of(
                "Bob", 2,
                "Charlie", 0
        )));

        assertEquals("Prediction for alias Bob cannot be greater than the current round number", e.getMessage());
    }

    // --------------------------
    // ---- Set Round Result ----
    // --------------------------

    @Test
    void game_setRoundResults_settingResultBeforePredictions() {
        // Given
        WizardsGame game = new WizardsGame("Test Game", Set.of("Bob", "Charlie"));

        // When / Then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> game.setRoundResult(Map.of(
                "Bob", 1,
                "Charlie", 0
        )));

        assertEquals("Predictions for round 1 have not been set", e.getMessage());
    }

    @Test
    void game_setRoundResults_resultsAreNotValidForRound() {
        // Given
        WizardsGame game = new WizardsGame("Test Game", Set.of("Bob", "Charlie"));

        game.setRoundPredictions(Map.of(
                "Bob", 1,
                "Charlie", 0
        ));

        //  When / Then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> game.setRoundResult(Map.of(
                "Bob", 1,
                "Charlie", 1
        )));

        assertEquals("The sum of results do not match the maximum round result", e.getMessage());
    }

    // ----------------------
    // ---- Game Process ----
    // ----------------------

    @Test
    void game_round_OK() {
        // Given
        WizardsGame game = new WizardsGame("Test Game", Set.of("Alice", "Bob", "Charlie"));

        // When

        // Round 1
        game.setRoundPredictions(Map.of(
                "Alice", 1,
                "Bob", 0,
                "Charlie", 1
        ));

        game.setRoundResult(Map.of(
                "Alice", 1,
                "Bob", 0,
                "Charlie", 0
        ));

        // Round 2
        game.setRoundPredictions(Map.of(
                "Alice", 1,
                "Bob", 0,
                "Charlie", 1
        ));

        game.setRoundResult(Map.of(
                "Alice", 2,
                "Bob", 0,
                "Charlie", 0
        ));


        // Then
        assertEquals(20, game.getScore().get("Alice").intValue());
        assertEquals(40, game.getScore().get("Bob").intValue());
        assertEquals(-20, game.getScore().get("Charlie").intValue());
        assertEquals(3, game.getRound());
        assertEquals(2, game.getScoreboard().size());
    }

    // ----------------------
    // ---- Game Is Over ----
    // ----------------------

    @Test
    void game_gameIsOver() {
        // Given
        WizardsGame game = new WizardsGame("Test Game", Set.of(
                "Bob",
                "Charlie",
                "George",
                "Mark",
                "Stacy",
                "Ron"
        ));

        for (int i = 1; i <= 10; i++) {
            game.setRoundPredictions(Map.of(
                    "Bob", i,
                    "Charlie", 0,
                    "George", 0,
                    "Mark", 0,
                    "Stacy", 0,
                    "Ron", 0
            ));

            game.setRoundResult(Map.of(
                    "Bob", i,
                    "Charlie", 0,
                    "George", 0,
                    "Mark", 0,
                    "Stacy", 0,
                    "Ron", 0
            ));


        }

        // When / Then
        IllegalStateException e = assertThrows(IllegalStateException.class, () ->
                game.setRoundPredictions(Map.of(
                        "Bob", 1,
                        "Charlie", 0,
                        "George", 0,
                        "Mark", 0,
                        "Stacy", 0,
                        "Ron", 0
                ))
        );

        assertEquals("The Game is over", e.getMessage());
    }
}
