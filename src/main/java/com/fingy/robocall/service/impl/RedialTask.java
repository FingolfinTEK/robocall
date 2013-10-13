package com.fingy.robocall.service.impl;

import com.fingy.robocall.dao.CallRequestRepository;
import com.fingy.robocall.model.CallRequest;
import com.fingy.robocall.model.dto.RoboCallRequest;
import com.fingy.robocall.service.ApplicationPropertiesHolder;
import com.fingy.robocall.service.TwilioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RedialTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CallRequest callRequest;
    private final TwilioService service;
    private final ApplicationPropertiesHolder propertiesHolder;

    public RedialTask(CallRequest callRequest, TwilioService service, ApplicationPropertiesHolder propertiesHolder) {
        this.callRequest = callRequest;
        this.service = service;
        this.propertiesHolder = propertiesHolder;
    }

    @Override
    public void run() {
        try {
            logger.info("Redialing call {}", callRequest.getSid());
            RoboCallRequest request = toRoboCallRequest(callRequest);
            CallRequest redialedCall = service.placeCall(request, callRequest.getRootUrl());
            updateRedialCount(callRequest, redialedCall);
        } catch (Exception e) {
            logger.error("Error redialing call " + callRequest.getSid(), e);
        }
    }

    private RoboCallRequest toRoboCallRequest(CallRequest callRequest) {
        return new RoboCallRequest(callRequest.getText(), callRequest.getLanguage(), callRequest.getPhoneNumber(), 1, propertiesHolder.getKey());
    }

    private void updateRedialCount(CallRequest callRequest, CallRequest redialedCall) {
        redialedCall.setRedialCount(callRequest.getRedialCount() + 1);
        service.save(redialedCall);
        service.delete(callRequest);
    }
}
