package com.gregorv.home.wizards.vo;

import tools.jackson.databind.ObjectMapper;

import java.math.BigInteger;
import java.util.Map;

public class GameSocketMessage {

    final String content;
    final GameSocketMessageType type;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private GameSocketMessage(GameSocketMessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    public static GameSocketMessage tableUpdate(Map<Integer, Map<String, Round>> content) {
        return new GameSocketMessage(GameSocketMessageType.TABLE_UPDATE, OBJECT_MAPPER.writeValueAsString(content));

    }

    public static GameSocketMessage gameFinish(Map<String, BigInteger> content) {
        return new GameSocketMessage(GameSocketMessageType.GAME_FINISH, OBJECT_MAPPER.writeValueAsString(content));
    }

    // Getters must be exposed for Jackson to serialize the object to JSON when sending it via WebSocket
    public String getContent() {
        return content;
    }

    public GameSocketMessageType getType() {
        return type;
    }

    private enum GameSocketMessageType {
        TABLE_UPDATE, GAME_FINISH;
    }

}
