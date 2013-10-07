package com.fingy.robocall.web.controller;

import com.fingy.robocall.util.JAXBUtil;
import com.fingy.robocall.util.RequestUtil;
import com.fingy.robocall.util.SerializationUtil;
import com.fingy.robocall.web.controller.dto.Response;
import com.fingy.robocall.web.controller.dto.RoboCallRequest;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Call;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.fingy.robocall.util.SerializationUtil.serializeToString;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/call")
public class TwilioController {
    private static final String URL_PARAM_NAME = "Url";
    private static final String TO_PARAM_NAME = "To";
    private static final String FROM_PARAM_NAME = "From";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private @Value("${twilio.sid}") String accountSid;
    private @Value("${twilio.auth}") String authenticationToken;
    private @Value("${twilio.phone}") String twilioPhoneNumber;

    @RequestMapping(value = "/text-to-speech", method = {GET, POST})
    public ResponseEntity<String> call(RoboCallRequest callRequest, HttpServletRequest request) throws TwilioRestException, UnsupportedEncodingException {
        logger.info("Received call request " + callRequest);
        List<NameValuePair> params = prepareCallParameters(callRequest, request);
        return new ResponseEntity<>(doCallAndReturnSID(params), HttpStatus.OK);
    }

    private String doCallAndReturnSID(List<NameValuePair> params) throws TwilioRestException {
        TwilioRestClient client = new TwilioRestClient(accountSid, authenticationToken);
        CallFactory callFactory = client.getAccount().getCallFactory();
        Call call = callFactory.create(params);
        return call.getSid();
    }

    private List<NameValuePair> prepareCallParameters(RoboCallRequest callRequest, HttpServletRequest request) throws UnsupportedEncodingException {
        List<NameValuePair> params = new ArrayList<>();

        String url = getTwilioCallCallbackUrl(callRequest, request);
        logger.info("Callback url is " + url);

        params.add(new BasicNameValuePair(URL_PARAM_NAME, url));
        params.add(new BasicNameValuePair(TO_PARAM_NAME, callRequest.getPhoneNumber()));
        params.add(new BasicNameValuePair(FROM_PARAM_NAME, twilioPhoneNumber));

        return params;
    }

    private String getTwilioCallCallbackUrl(RoboCallRequest callRequest, HttpServletRequest request) throws UnsupportedEncodingException {
        String url = RequestUtil.getRequestPathBeforeFragment(request, "/text-to-speech") + "/twilio-callback?";
        return url + callRequest.toQueryParamString();
    }

    @RequestMapping(value = "/twilio-callback", method = {GET, POST})
    public ResponseEntity<String> redirectToNuance(RoboCallRequest callRequest, HttpServletRequest request) throws Exception {
        logger.info("Received callback " + callRequest);
        String nuanceUrl = getNuanceControllerConversionUrl(callRequest, request);
        return createResponse(JAXBUtil.marshallToString(new Response(nuanceUrl)));
    }

    private String getNuanceControllerConversionUrl(RoboCallRequest callRequest, HttpServletRequest request) throws IOException {
        String url = RequestUtil.getRequestPathBeforeFragment(request, "/call") + "/text-to-speech/convert/";
        return url + RequestUtil.encode(serializeToString(callRequest));
    }

    private ResponseEntity<String> createResponse(String twiMLResponse) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_XML);
        return new ResponseEntity<>(twiMLResponse, headers, HttpStatus.OK);
    }

}
