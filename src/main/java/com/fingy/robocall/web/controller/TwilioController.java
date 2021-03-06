package com.fingy.robocall.web.controller;

import com.fingy.robocall.model.CallRequest;
import com.fingy.robocall.model.dto.Response;
import com.fingy.robocall.model.dto.RoboCallRequest;
import com.fingy.robocall.service.TwilioService;
import com.fingy.robocall.util.JAXBUtil;
import com.fingy.robocall.util.RequestUtil;
import com.twilio.sdk.TwilioRestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static com.fingy.robocall.util.SerializationUtil.serializeToString;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/call")
public class TwilioController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private @Autowired TwilioService twilioService;

    @RequestMapping(value = "/text-to-speech", method = {GET, POST})
    public ResponseEntity<String> call(RoboCallRequest callRequest, HttpServletRequest request) throws TwilioRestException, UnsupportedEncodingException {
        String rootUrl = RequestUtil.getRequestPathBeforeFragment(request, "/text-to-speech");
        logger.info("Received call request " + callRequest);

        CallRequest requestInfo = twilioService.placeCall(callRequest, rootUrl);
        return new ResponseEntity<>(requestInfo.getSid(), HttpStatus.OK);
    }

    @RequestMapping(value = "/twilio-callback", method = {GET, POST})
    public ResponseEntity<String> redirectToNuance(RoboCallRequest callRequest, HttpServletRequest request) throws Exception {
        logger.info("Received callback " + callRequest);
        String nuanceUrl = getNuanceControllerConversionUrl(callRequest, request);
        return createResponse(JAXBUtil.marshallToString(new Response(nuanceUrl, callRequest.getLoop())));
    }

    @RequestMapping(value = "/twilio-status-callback", method = {GET, POST})
    public ResponseEntity<String> statusCallback(@RequestParam("CallSid") String callSid, @RequestParam("CallStatus") String callStatus) throws Exception {
        logger.info("Received status callback for call {}, with status {}", callSid, callStatus);
        twilioService.statusUpdate(callSid, callStatus);
        return new ResponseEntity<>(callSid, HttpStatus.OK);
    }

    private String getNuanceControllerConversionUrl(RoboCallRequest callRequest, HttpServletRequest request) throws IOException {
        Map<String,String[]> parameterMap = request.getParameterMap();
        String callSid = parameterMap.containsKey("CallSid") ? parameterMap.get("CallSid")[0] : "";
        String url = RequestUtil.getRequestPathBeforeFragment(request, "/call") + "/text-to-speech/convert/";
        return url + RequestUtil.encode(serializeToString(callRequest)) + "/" + callSid;
    }

    private ResponseEntity<String> createResponse(String twiMLResponse) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_XML);
        return new ResponseEntity<>(twiMLResponse, headers, HttpStatus.OK);
    }

}
