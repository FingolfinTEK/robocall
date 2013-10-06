package com.fingy.robocall.util;

import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class HttpClientUtil {

    public static PoolingClientConnectionManager createPoolingClientConnectionManager() {
        try {
            TrustManager trustManager = new TrustAllCertificates();
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, null);

            SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext,  new VerifyAllVerifier());

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
            registry.register(new Scheme("http", 8080, PlainSocketFactory.getSocketFactory()));
            registry.register(new Scheme("https", 443, sslSocketFactory));

            PoolingClientConnectionManager manager = new PoolingClientConnectionManager(registry);
            manager.setDefaultMaxPerRoute(10);
            return manager;
        } catch (NoSuchAlgorithmException | KeyManagementException ignored) {
        }

        return new PoolingClientConnectionManager();

    }

    public static HttpClient createDefaultHttpClient(PoolingClientConnectionManager manager) {
        HttpParams params = getDefaultHttpParams();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(manager, params);
        return setUpProxyIfNeeded(defaultHttpClient);
    }

    private static HttpParams getDefaultHttpParams() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(params, false);
        return params;
    }

    private static HttpClient setUpProxyIfNeeded(DefaultHttpClient defaultHttpClient) {
        String proxyPort = System.getProperty("http.proxyPort");
        String proxyHost = System.getProperty("http.proxyHost");

        if (proxyPort != null && proxyHost != null) {
            HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
            defaultHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        return defaultHttpClient;
    }

    private static class VerifyAllVerifier implements X509HostnameVerifier {
        @Override
        public void verify(String string, SSLSocket ssls) throws IOException {
        }

        @Override
        public void verify(String string, X509Certificate xc) throws SSLException {
        }

        @Override
        public void verify(String string, String[] strings, String[] strings1) throws SSLException {
        }

        @Override
        public boolean verify(String string, SSLSession sslSession) {
            return true;
        }
    }
}
