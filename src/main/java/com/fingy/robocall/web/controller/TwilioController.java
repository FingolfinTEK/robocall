package com.fingy.robocall.web.controller;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Call;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/call")
public class TwilioController {
    private static final String URL_PARAM_NAME = "Url";
    private static final String TO_PARAM_NAME = "To";
    private static final String FROM_PARAM_NAME = "From";

    private @Value("${twilio.sid}") String accountSid;
    private @Value("${twilio.auth}") String authenticationToken;

    @RequestMapping("/text-to-speech")
    public ResponseEntity<String> call() throws TwilioRestException {
        List<NameValuePair> params = prepareCallParameters();
        String callSid = doCall(params);
        return new ResponseEntity<>(callSid, HttpStatus.OK);
    }

    private String doCall(List<NameValuePair> params) throws TwilioRestException {
        TwilioRestClient client = new TwilioRestClient(accountSid, authenticationToken);
        CallFactory callFactory = client.getAccount().getCallFactory();
        Call call = callFactory.create(params);
        return call.getSid();
    }

    private List<NameValuePair> prepareCallParameters() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(URL_PARAM_NAME, "http://demo.twilio.com/docs/voice.xml")); // TODO get from HTTP request
        params.add(new BasicNameValuePair(TO_PARAM_NAME, "+14155551212"));     //TODO use argument
        params.add(new BasicNameValuePair(FROM_PARAM_NAME, "+14158675309")); //TODO add to config
        return params;
    }
}
