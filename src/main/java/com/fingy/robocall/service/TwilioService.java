package com.fingy.robocall.service;

import com.fingy.robocall.model.CallRequest;
import com.fingy.robocall.model.dto.RoboCallRequest;
import com.twilio.sdk.TwilioRestException;

import java.io.UnsupportedEncodingException;

public interface TwilioService {

    CallRequest placeCall(RoboCallRequest callRequest, String callbackUrl) throws TwilioRestException, UnsupportedEncodingException;

    void save(CallRequest redialedCall);

    void delete(CallRequest callRequest);

    void scheduleRedial(String callSid);

    void statusUpdate(String callSid, String callStatus);
}
