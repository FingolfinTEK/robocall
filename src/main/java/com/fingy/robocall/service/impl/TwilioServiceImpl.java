package com.fingy.robocall.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.fingy.robocall.model.CallRequest;
import com.fingy.robocall.model.dto.RoboCallRequest;
import com.fingy.robocall.web.controller.TwilioController;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Call;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioServiceImpl implements TwilioService {

    private static final String URL_PARAM_NAME = "Url";
    private static final String TO_PARAM_NAME = "To";
    private static final String FROM_PARAM_NAME = "From";

    @Value("${twilio.sid}")
    private String accountSid;
    @Value("${twilio.auth}")
    private String authenticationToken;
    @Value("${twilio.phone}")
    private String twilioPhoneNumber;

    @Override
    public CallRequest placeCall(RoboCallRequest callRequest, String callbackUrl) throws TwilioRestException {
        List<NameValuePair> params = createCallParameters(callRequest, callbackUrl);
        String sid = placeCall(params);
        return new CallRequest(sid, callRequest.getText(), callRequest.getLanguage());
    }

    private String placeCall(List<NameValuePair> params) throws TwilioRestException {
        TwilioRestClient client = new TwilioRestClient(accountSid, authenticationToken);
        CallFactory callFactory = client.getAccount().getCallFactory();
        Call call = callFactory.create(params);
        return call.getSid();
    }

    private List<NameValuePair> createCallParameters(RoboCallRequest callRequest, String url) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair(URL_PARAM_NAME, url));
        params.add(new BasicNameValuePair(TO_PARAM_NAME, callRequest.getPhoneNumber()));
        params.add(new BasicNameValuePair(FROM_PARAM_NAME, twilioPhoneNumber));

        return params;
    }
}