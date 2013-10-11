/*
 * TTSHTTPClient.java
 *
 * This is a simple command-line java app that shows how to use the NMDP HTTP Client Interface for
 *	Text-to-Speech (TTS) requests using the POST method
 *
 * This basic java app will:
 *	1. Create an instance of an HttpClient to interact with our HTTP Client Interface for TTS
 *	2. Use some simple helper methods to setup the URI and HTTP POST parameters
 *	3. Execute the HTTP Request
 *	4. Process the HTTP Response, writing the generated audio to file
 *
 *	Output of progress of the request is logged to console
 *	Values to be passed to the HTTP Client Interface are simply hard-coded class members for demo purposes
 *
 * @copyright  Copyright (c) 2010 Nuance Communications, inc. (http://www.nuance.com)
 *
 * @Created	: April 28, 2011
 * @Author	: Peter Freshman
 */
package com.fingy.robocall.service.impl;

import com.fingy.robocall.service.NuanceTextToSpeechService;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fingy.robocall.util.HttpClientUtil.createDefaultHttpClient;
import static com.fingy.robocall.util.HttpClientUtil.createPoolingClientConnectionManager;


@Service
public class NuanceTextToSpeechServiceImpl implements NuanceTextToSpeechService {

    private static final Pattern HOST_PATTERN = Pattern.compile("(http|https)://([^:]+):(\\d+)");
    private static final String CODEC = "audio/x-wav";
    private static final String TTS = "/NMDPTTSCmdServlet/tts";
    private static final String APP_ID_PARAM_NAME = "appId";
    private static final String APP_KEY_PARAM_NAME = "appKey";
    private static final String TTS_LANG_PARAM_NAME = "ttsLang";
    public static final String SESSIONID_HEADER_NAME = "x-nuance-sessionid";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ClientConnectionManager manager = createPoolingClientConnectionManager();

    private @Value("${nuance.host}") String host;
    private @Value("${nuance.aid}") String applicationId;
    private @Value("${nuance.key}") String applicationKey;

    public NuanceTextToSpeechServiceImpl() {
    }

    public NuanceTextToSpeechServiceImpl(String host, String applicationId, String applicationKey) {
        this.host = host;
        this.applicationId = applicationId;
        this.applicationKey = applicationKey;
    }

    @Override
    public String convertToSpeech(String text, String language, OutputStream outputStream) throws IOException, URISyntaxException {
        HttpPost conversionRequest = createRequest(language, text);
        HttpClient httpClient = createDefaultHttpClient(manager);
        System.out.println("executing request " + conversionRequest.getRequestLine());
        HttpResponse response = httpClient.execute(conversionRequest);
        return processResponse(response, outputStream);
    }

    private HttpPost createRequest(String language, String text) throws UnsupportedEncodingException, URISyntaxException {
        HttpPost httppost = new HttpPost(getURI(language));
        httppost.addHeader("Content-Type", "text/plain");
        httppost.addHeader("Accept", CODEC);
        HttpEntity entity = new StringEntity(text, "utf-8");
        httppost.setEntity(entity);
        return httppost;
    }

    private URI getURI(String language) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(determineScheme());
        uriBuilder.setHost(determineHost());
        uriBuilder.setPort(determinePort());
        uriBuilder.setPath(TTS);

        uriBuilder.addParameter(APP_ID_PARAM_NAME, applicationId);
        uriBuilder.addParameter(APP_KEY_PARAM_NAME, applicationKey);
        uriBuilder.addParameter(TTS_LANG_PARAM_NAME, language);

        return uriBuilder.build();
    }

    private String determineScheme() {
        return matchAndExtractGroup(1, "https");
    }

    private String matchAndExtractGroup(int group, String defaultValue) {
        Matcher matcher = HOST_PATTERN.matcher(host);
        if (matcher.matches()) {
            return matcher.group(group);
        }
        return defaultValue;
    }

    private String determineHost() {
        return matchAndExtractGroup(2, "");
    }

    private int determinePort() {
        return Integer.parseInt(matchAndExtractGroup(3, "443"));
    }

    private String processResponse(HttpResponse response, OutputStream outputStream) throws IllegalStateException, IOException {
        Header sessionId = response.getFirstHeader(SESSIONID_HEADER_NAME);
        logger.info("Session ID header: " + sessionId);

        HttpEntity resEntity = response.getEntity();
        logger.info("Received response with status {} and content length {}", response.getStatusLine(), resEntity.getContentLength());

        copyResultIntoOutputStream(resEntity, outputStream);

        String contentType = resEntity.getContentType().getValue();
        EntityUtils.consume(resEntity);
        return contentType;
    }

    private void copyResultIntoOutputStream(HttpEntity resEntity, OutputStream outputStream) {
        try {
            IOUtils.copy(resEntity.getContent(), outputStream);
        } catch (Exception e) {
            logger.error("Error reading response ", e);
        }
    }
}
