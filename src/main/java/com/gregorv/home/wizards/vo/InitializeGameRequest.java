package com.gregorv.home.wizards.vo;

import com.gregorv.home.wizards.exception.ValidationException;

import java.util.Set;

public record InitializeGameRequest(
        int numberOfPlayers,
        Set<String> aliases
) {

    public InitializeGameRequest{
        if(numberOfPlayers < 2){
            throw new ValidationException("At least two players are required to start the game.");
        }

        if(numberOfPlayers > 6){
            throw new ValidationException("No more than 6 players can play at a time.");
        }

        if(aliases == null || aliases.isEmpty()){
            throw new ValidationException("Aliases are required.");
        }

        if(aliases.size() != numberOfPlayers){
            throw new ValidationException("The number of UNIQUE aliases must be equal to number of players.");
        }
    }
}
