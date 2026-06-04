package com.gregorv.home.wizards.vo;

import org.jspecify.annotations.NonNull;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WizardsGame {
    private final String name;
    private final Set<String> aliases;
    private int round = 1;
    private final Map<String, BigInteger> score = new HashMap<>();
    private final Map<Integer, Map<String, Round>> scoreboard = new HashMap<>();

    public WizardsGame(@NonNull String name, @NonNull Set<String> aliases) {

        if (aliases.isEmpty() || aliases.size() < 2) {
            throw new IllegalArgumentException("At least 2 aliases are required");
        }

        if (aliases.size() > 6) {
            throw new IllegalArgumentException("At most 6 aliases are required");
        }

        this.name = name;
        this.aliases = aliases;

        aliases.forEach(alias -> score.put(alias, BigInteger.ZERO));
    }

    public void setRoundPredictions(@NonNull Map<String, Integer> predictions) {
        checkIfTheGameIsNotOver();

        if (scoreboard.get(round) != null) {
            throw new IllegalStateException("Predictions for round " + round + " have already been set");
        }

        checkIfActionContainsAllTheActors(predictions.keySet());

        Map<String, Round> rounds = new HashMap<>();
        predictions.forEach((alias, prediction) -> {
            if (prediction.compareTo(round) > 0) {
                throw new IllegalArgumentException("Prediction for alias " + alias + " cannot be greater than the current round number");
            }
            rounds.put(alias, new Round(prediction));

        });

        scoreboard.put(round, rounds);
    }

    public void setRoundResult(@NonNull Map<String, Integer> results) {
        checkIfTheGameIsNotOver();

        checkIfActionContainsAllTheActors(results.keySet());

        if (scoreboard.get(round) == null) {
            throw new IllegalStateException("Predictions for round " + round + " have not been set");
        }

        validateProvidedResult(results);

        Map<String, Round> aliasPredictionsForRound = scoreboard.get(round);

        results.forEach((alias, rez) -> {
            Round r = aliasPredictionsForRound.get(alias);
            r.setResult(rez);
            score.computeIfPresent(alias, (k, s) -> s.add(r.getScore()));
        });

        round++;
    }

    private void checkIfTheGameIsNotOver() {
        if (round > gameNumberOfRounds()) {
            throw new IllegalStateException("The Game is over");
        }
    }

    private void checkIfActionContainsAllTheActors(Set<String> providedActors) {
        if (!providedActors.containsAll(aliases)) {
            throw new IllegalArgumentException("Predictions must be provided for all aliases");
        }

        providedActors.forEach(actor -> {
            if (!aliases.contains(actor)) {
                throw new IllegalArgumentException("Unknown alias: " + actor);
            }
        });
    }

    private void validateProvidedResult(Map<String, Integer> results) {
        int validateResults = 0;
        for (Integer v : results.values()) {
            validateResults += v;
        }

        if (validateResults != round) {
            throw new IllegalStateException("The sum of results do not match the maximum round result");
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public int getRound() {
        return round;
    }

    public Map<String, BigInteger> getScore() {
        return score;
    }

    public Map<Integer, Map<String, Round>> getScoreboard() {
        return scoreboard;
    }

    public int gameNumberOfRounds() {
        return switch (aliases.size()) {
            case 6 -> 10;
            case 5 -> 12;
            case 4 -> 15;
            default -> 20;
        };
    }
}
