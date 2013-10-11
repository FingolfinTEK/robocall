package com.fingy.robocall.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import com.fingy.robocall.model.dto.RoboCallRequest;
import com.fingy.robocall.service.NuanceTextToSpeechService;
import com.fingy.robocall.service.TwilioService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.fingy.robocall.util.SerializationUtil.deserializeFromString;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/text-to-speech")
public class NuanceController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private @Value("${application.key}") String key;

    private @Autowired TwilioService twilioService;
    private @Autowired NuanceTextToSpeechService textToSpeechService;

    @RequestMapping(value = "/convert", method = {GET, POST})
    public ResponseEntity<byte[]> convert(RoboCallRequest conversionRequest, @RequestParam(value = "CallSid", required = false) String callSid) {
        try {
            logger.info("Received conversion request " + conversionRequest);
            return doConvert(conversionRequest);
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            if (StringUtils.isNotBlank(callSid)) {
                twilioService.scheduleRedial(callSid);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<byte[]> doConvert(RoboCallRequest request) throws IOException, URISyntaxException {
        if (key.equals(request.getKey())) {
            try (ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
                String contentType = textToSpeechService.convertToSpeech(request.getText(), request.getLanguage(), bytes);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(contentType));
                return new ResponseEntity<>(bytes.toByteArray(), headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/convert/{conversionRequest}/{callSid}", method = {GET, POST})
    public ResponseEntity<byte[]> convert(@PathVariable("conversionRequest") String conversionRequest, @PathVariable("callSid") String callSid) throws IOException,
            ClassNotFoundException {
        logger.info("Received conversion request {}, {}", conversionRequest, callSid);
        return convert((RoboCallRequest) deserializeFromString(conversionRequest), callSid);
    }
}
