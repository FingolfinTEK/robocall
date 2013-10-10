package com.fingy.robocall.model.dto;

import com.fingy.robocall.util.RequestUtil;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import static com.fingy.robocall.util.RequestUtil.encode;

public class RoboCallRequest implements Serializable {

    private String text;
    private String language;
    private String phoneNumber;
    private String key;

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
