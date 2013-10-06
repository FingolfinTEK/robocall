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
package com.fingy.robocall.service;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
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
public class NuanceTextToSpeechService {

    private static final Pattern HOST_PATTERN = Pattern.compile("([htps]+)://([^:]+):(\\d+)");
    private static final String CODEC = "audio/x-wav";
    private static final String TTS = "/NMDPTTSCmdServlet/tts";

    private final PoolingClientConnectionManager manager = createPoolingClientConnectionManager();

    private @Value("nuance.host") String host;
    private @Value("nuance.aid") String applicationId;
    private @Value("nuance.key") String applicationKey;

    public NuanceTextToSpeechService(String host, String applicationId, String applicationKey) {
        this.host = host;
        this.applicationId = applicationId;
        this.applicationKey = applicationKey;
    }

    public void convertToSpeech(String text, String language, OutputStream outputStream) throws IOException, URISyntaxException {
        HttpPost conversionRequest = createRequest(language, text);
        HttpClient httpClient = createDefaultHttpClient(manager);
        System.out.println("executing request " + conversionRequest.getRequestLine());
        HttpResponse response = httpClient.execute(conversionRequest);
        processResponse(response, outputStream);
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

        uriBuilder.addParameter("appId", applicationId);
        uriBuilder.addParameter("appKey", applicationKey);
        uriBuilder.addParameter("ttsLang", language);

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

    private void processResponse(HttpResponse response, OutputStream outputStream) throws IllegalStateException, IOException {
        HttpEntity resEntity = response.getEntity();
        Header sessionId = response.getFirstHeader("x-nuance-sessionid");
        if (sessionId != null)
            System.out.println("x-nuance-sessionid: " + sessionId.getValue());

        String status = response.getStatusLine().toString();
        boolean okFound = status.contains("200 OK");
        if (okFound) {
            System.out.println("Response content length: " + resEntity.getContentLength());
            System.out.println("Chunked?: " + resEntity.isChunked());
        }

        try {
            IOUtils.copy(resEntity.getContent(), outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntityUtils.consume(resEntity);
    }

    public static void main(String... args) throws Exception {
        NuanceTextToSpeechService service = new NuanceTextToSpeechService(args[0], args[1], args[2]);
        service.convertToSpeech(args[3], "en_US", new FileOutputStream("result.wav"));
    }
}
