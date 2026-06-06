package com.gregorv.home.wizards.vo;

public class HelloMessage {

    private String name;

    public HelloMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public HelloMessage setName(String name) {
        this.name = name;
        return this;
    }
}
