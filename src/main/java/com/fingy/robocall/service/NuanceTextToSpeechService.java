package com.fingy.robocall.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public interface NuanceTextToSpeechService {

    String convertToSpeech(String text, String language, OutputStream outputStream) throws IOException, URISyntaxException;
}
