package com.gregorv.home.wizards.vo;

import com.gregorv.home.wizards.exception.ValidationException;

import java.util.Set;

public record InitializeGameRequest(
        String gameName,
        Set<String> aliases
) {

    public InitializeGameRequest {
        if (gameName == null || gameName.isBlank()) {
            throw new ValidationException("Game name is required.");
        }

        if (aliases == null || aliases.isEmpty()) {
            throw new ValidationException("Aliases are required.");
        }

        if (aliases.size() < 2 || aliases.size() > 6) {
            throw new ValidationException("At least 2 and at most 6 aliases are required.");
        }
    }
}
