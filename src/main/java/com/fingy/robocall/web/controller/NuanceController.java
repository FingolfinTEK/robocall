package com.fingy.robocall.web.controller;

import com.fingy.robocall.service.NuanceTextToSpeechService;
import com.fingy.robocall.web.controller.dto.RoboCallRequest;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.fingy.robocall.util.SerializationUtil.deserializeFromString;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/text-to-speech")
public class NuanceController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private @Value("${application.key}") String key;
    private @Autowired NuanceTextToSpeechService textToSpeechService;

    @RequestMapping(value = "/convert", method = {GET, POST})
    public ResponseEntity<byte[]> convert(RoboCallRequest conversionRequest) {
        try {
            logger.info("Received conversion request " + conversionRequest);
            return doConvert(conversionRequest);
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<byte[]> doConvert(RoboCallRequest conversionRequest) throws IOException, URISyntaxException {
        if (key.equals(conversionRequest.getKey())) {
            try (ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
                String contentType = textToSpeechService.convertToSpeech(conversionRequest.getText(), conversionRequest.getLanguage(), bytes);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(contentType));
                return new ResponseEntity<>(bytes.toByteArray(), headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/convert/{conversionRequest}", method = {GET, POST})
    public ResponseEntity<byte[]> convert(@PathVariable("conversionRequest") String conversionRequest) throws IOException, ClassNotFoundException {
        logger.info("Received conversion request " + conversionRequest);
        return convert((RoboCallRequest) deserializeFromString(conversionRequest));
    }
}
