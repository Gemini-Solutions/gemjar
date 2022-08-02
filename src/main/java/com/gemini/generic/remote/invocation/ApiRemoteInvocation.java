package com.gemini.generic.remote.invocation;


import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.gemini.generic.utils.EnumUtils;
import com.gemini.generic.utils.GemConstants;
import com.gemini.generic.utils.GemUtils;


public class ApiRemoteInvocation  {

    private final static Logger logger = Logger.getLogger(ApiRemoteInvocation.class);


    static class MyAuthenticator extends Authenticator {
        static final String kuser = ""; // your account name
        static final String kpass = ""; // your account password

        public PasswordAuthentication getPasswordAuthentication() {
            // System.out.println("Using Custom Authentication");
            String decryptedPwd = GemUtils.getDecryptedPwd(kpass);
            return (new PasswordAuthentication(kuser, decryptedPwd.toCharArray()));
        }
    }



    /////////////////////////////////////////////// HTTPS OPERATION
    /////////////////////////////////////////////// //////////////////////////////
    private static TrustManager[] getTrustManager() {
        TrustManager[] trustManager = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // TODO Auto-generated method stub

            }

        }

        };
        return trustManager;
    }

    private static HostnameVerifier getHostVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        return hostnameVerifier;
    }

    private static HttpURLConnection createSSLDisabledHttpsUrlConnection(final URL requestUrl) {
        HttpsURLConnection httpsURLConnection;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            httpsURLConnection = (HttpsURLConnection) requestUrl.openConnection();
            httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            httpsURLConnection.setHostnameVerifier(getHostVerifier());
            httpsURLConnection.setDoOutput(true);
        } catch (Exception e) {
            httpsURLConnection = null;
            logger.info("Exception Occured During SSL Connection Creation");
        }
        return httpsURLConnection;
    }

    public static class RequestBuilder {

        public Object build(Request request) {

            String url = StringUtils.replace(request.getURL(), " ", "%20");
            Authenticator.setDefault(new ApiRemoteInvocation.MyAuthenticator());
            String method = request.getMethod().toUpperCase();
            method = method.toUpperCase();
            HttpURLConnection httpsCon = null;
            URL requestUrl = null;
            try {
                requestUrl = new URL(url);
                String requestProtocol = requestUrl.getProtocol();
                if (StringUtils.isNotBlank(requestProtocol)) {
                    httpsCon = requestProtocol.equals("https") ? createSSLDisabledHttpsUrlConnection(requestUrl) : (HttpURLConnection) requestUrl.openConnection();
                }
            } catch (MalformedURLException e) {
                logger.info("URL Malformed");
                throw new RuntimeException(e);
            } catch (IOException e) {
                logger.info("I/O Exception Occured");
                throw new RuntimeException(e);
            }

            httpsCon.setRequestProperty("Content-Type", "application/json");
            if (StringUtils.isNotBlank(request.getContentType()) && !StringUtils.contains(request.getContentType(), "json")) {
                httpsCon.setRequestProperty("Content-Type", "multipart/form-data");
            }
            httpsCon.setDoOutput(true);
            httpsCon.setRequestProperty("accept", "application/json, text/plain, */*");
            httpsCon.setRequestProperty("Connection", "keep-alive");
            httpsCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");
            if (request.getHeaderMap() != null) {
                for (Map.Entry<String, String> set : request.getHeaderMap().entrySet()) {
                    httpsCon.setRequestProperty(set.getKey(), set.getValue());
                }

            }
            if (GemConstants.GET.equalsIgnoreCase(method) || GemConstants.DELETE.equalsIgnoreCase(method)) {
                try {
                    httpsCon.setRequestMethod(method);
                } catch (ProtocolException e) {
                    logger.info("Run Time/Protocol Exception Occured");
                    throw new RuntimeException(e);
                }
            }
            if (GemConstants.PATCH.equalsIgnoreCase(method)) {
                httpsCon.setRequestProperty("X-HTTP-Method-Override", GemConstants.PATCH);
            }
            if (GemConstants.PUT.equalsIgnoreCase(method) || GemConstants.POST.equalsIgnoreCase(method) || GemConstants.PATCH.equalsIgnoreCase(method)) {
                try {
                    if (GemConstants.PATCH.equalsIgnoreCase(method)) {
                        httpsCon.setRequestMethod(GemConstants.POST);
                    } else {
                        httpsCon.setRequestMethod(method);
                    }
                    httpsCon.setReadTimeout(GemConstants.readTimeOut);
                    GemUtils.writeDataToOutputStream(httpsCon.getOutputStream(), request.getRequestPayload());
                } catch (ProtocolException e) {
                    logger.info("Run Time/Protocol Exception Occured");
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    logger.info("I/O Exception Occured");
                    throw new RuntimeException(e);
                }

            }
            return null;
        }
    }
    // Main Function to execute the request as per requirement
    private static Response executeRequest(Request request) {

        long startTime = Instant.now().toEpochMilli();
        String requestHeaders = "";
        Response responseJSON = null;
        try {
            Object o = new ApiRemoteInvocation.RequestBuilder().build(request);
            HttpURLConnection httpsCon = ((HttpURLConnection) o);
            requestHeaders = httpsCon.getHeaderFields().toString();
            httpsCon.connect();
            responseJSON = new Response(httpsCon, startTime, requestHeaders);
            if (StringUtils.isNotBlank(request.getStep())) {
                ApiRemoteInvocationImpl.doReporting(responseJSON, request);
            }
            return responseJSON;

        } catch (Exception e) {
            logger.info(" Exception Occured while posting Request");
            return null;
        }
    }

    //Utility Method to Execute HTTP request PUT/POST/DELETE/PATCH/CREATE
    public static Response handleRequest(Request request) throws Exception {
        RequestType command=RequestType.valueOf(EnumUtils.toEnumLookupValue(request.getMethod()));
        if(null== command){
            logger.info("Unknown Request Type");
            throw new Exception("Unknown Request Type");
        }
        return command.executeHttpRequest(request);
    }
}