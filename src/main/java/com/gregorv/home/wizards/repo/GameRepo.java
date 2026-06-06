package com.gregorv.home.wizards.repo;

import com.gregorv.home.wizards.vo.WizardsGame;

import java.util.Map;
import java.util.Set;

public interface GameRepo {

    void initGame(String gameName, Set<String> aliases);

    WizardsGame getGame(String gameName);

    void setGameRoundPrediction(String gameName, Map<String, Integer> roundPrediction);

    void setGameRoundResult(String gameName, Map<String, Integer> roundResult);
}
