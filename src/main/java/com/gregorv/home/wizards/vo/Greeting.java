package com.gregorv.home.wizards.vo;

public class Greeting {

    String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Greeting setContent(String content) {
        this.content = content;
        return this;
    }
}
