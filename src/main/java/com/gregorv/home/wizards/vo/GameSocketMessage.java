package com.gregorv.home.wizards.vo;

public class GameSocketMessage {

    String content;
    String type;

    public GameSocketMessage() {
    }

    public GameSocketMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public GameSocketMessage setContent(String content) {
        this.content = content;
        return this;
    }

    public String getType() {
        return type;
    }

    public GameSocketMessage setType(String type) {
        this.type = type;
        return this;
    }
}
