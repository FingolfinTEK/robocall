package com.fingy.robocall.util;

import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RequestUtil {

    public static String getRequestPathBeforeFragment(HttpServletRequest request, String urlFragment) {
        String url = request.getRequestURL().toString();
        return url.split(urlFragment)[0];
    }

    public static String encode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "ISO-8859-1");
    }
}
