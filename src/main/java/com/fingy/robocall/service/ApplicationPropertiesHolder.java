package com.fingy.robocall.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationPropertiesHolder {

    private @Value("${application.key}") String key;
    private @Value("${twilio.sid}") String accountSid;
    private @Value("${twilio.auth}") String authenticationToken;
    private @Value("${twilio.phone}") String twilioPhoneNumber;
    private @Value("${twilio.redialThreshold}") Integer redialThreshold;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public String getTwilioPhoneNumber() {
        return twilioPhoneNumber;
    }

    public void setTwilioPhoneNumber(String twilioPhoneNumber) {
        this.twilioPhoneNumber = twilioPhoneNumber;
    }

    public Integer getRedialThreshold() {
        return redialThreshold;
    }

    public void setRedialThreshold(Integer redialThreshold) {
        this.redialThreshold = redialThreshold;
    }
}
