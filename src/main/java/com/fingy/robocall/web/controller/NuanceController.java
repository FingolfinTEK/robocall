package com.fingy.robocall.web.controller;

import com.fingy.robocall.service.NuanceTextToSpeechService;
import com.fingy.robocall.web.controller.dto.ConversionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/text-to-speech")
public class NuanceController {

    private @Value("application.key") String key;
    private @Autowired NuanceTextToSpeechService textToSpeechService;

    @RequestMapping(value = "/convert", method = RequestMethod.GET)
    public void convert(ConversionRequest conversionRequest, HttpServletResponse response) {
        try {
            doConvert(conversionRequest, response);
        } catch (IOException | URISyntaxException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void doConvert(ConversionRequest conversionRequest, HttpServletResponse response) throws IOException, URISyntaxException {
        if (key.equals(conversionRequest.getKey())) {
            textToSpeechService.convertToSpeech(conversionRequest.getText(), conversionRequest.getLanguage(), response.getOutputStream());
            response.flushBuffer();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
