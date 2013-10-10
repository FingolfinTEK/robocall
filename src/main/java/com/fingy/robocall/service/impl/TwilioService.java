package com.fingy.robocall.service.impl;

import com.fingy.robocall.model.CallRequest;
import com.fingy.robocall.model.dto.RoboCallRequest;
import com.twilio.sdk.TwilioRestException;

public interface TwilioService {
    CallRequest placeCall(RoboCallRequest callRequest, String callbackUrl) throws TwilioRestException;
}
