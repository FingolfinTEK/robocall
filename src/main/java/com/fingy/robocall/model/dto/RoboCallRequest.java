package com.fingy.robocall.model.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import static com.fingy.robocall.util.RequestUtil.encode;

public class RoboCallRequest implements Serializable {

    private String text;
    private String language;
    private String phoneNumber;
    private Integer loop;
    private String key;

    public RoboCallRequest() {
    }

    public RoboCallRequest(String text, String language, String phoneNumber, Integer loop, String key) {
        this.text = text;
        this.language = language;
        this.phoneNumber = phoneNumber;
        this.loop = loop;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getLoop() {
        return loop;
    }

    public void setLoop(Integer loop) {
        this.loop = loop;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toQueryParamString() throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        builder.append("text=").append(encode(text));
        builder.append("&");
        builder.append("language=").append(encode(language));
        builder.append("&");
        builder.append("key=").append(encode(key));

        if (loop != null) {
            builder.append("&loop=").append(loop);
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return "RoboCallRequest{" +
                "text='" + text + '\'' +
                ", language='" + language + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
