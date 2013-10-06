package com.fingy.robocall.web.controller.dto;

public class ConversionRequest {

    private String text;
    private String language;
    private String key;

    public ConversionRequest() {
    }

    public ConversionRequest(String text, String language, String key) {
        this.text = text;
        this.language = language;
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
