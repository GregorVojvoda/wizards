package com.gregorv.home.wizards.repo;

import com.gregorv.home.wizards.vo.WizardsGame;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameRepoLocal implements GameRepo {

    private final Map<String, WizardsGame> games = new HashMap<>();

    @Override
    public void initGame(String gameName, Set<String> aliases) {
        if(games.get(gameName) != null){
            throw new IllegalStateException("Game with name '%s' already exists".formatted(gameName));
        }
        WizardsGame game = new WizardsGame(gameName, aliases);
        games.put(gameName, game);
    }

    @Override
    public WizardsGame getGame(String gameName) {
        return games.get(gameName);
    }

    @Override
    public void setGameRoundPrediction(String gameName, Map<String, Integer> roundPrediction) {
        WizardsGame game = games.get(gameName);
        game.setRoundPredictions(roundPrediction);
    }

    @Override
    public void setGameRoundResult(String gameName, Map<String, Integer> roundResult) {
        WizardsGame game = games.get(gameName);
        game.setRoundResult(roundResult);
    }


}
