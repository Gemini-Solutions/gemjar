package com.gemini.apitest;

import com.gemini.generic.ParameterizedUrl;
import com.gemini.generic.QuanticGlobalVar;
import com.gemini.quartzReporting.GemTestReporter;
import com.gemini.quartzReporting.STATUS;
import com.google.gson.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ApiClientConnect {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static class MyAuthenticator extends Authenticator {
        static final String kuser = ""; // your account name
        static final String kpass = ""; // your account password

        public PasswordAuthentication getPasswordAuthentication() {
            // System.out.println("Using Custom Authentication");
            String decryptedPwd = getDecryptedPwd(kpass);
            return (new PasswordAuthentication(kuser, decryptedPwd.toCharArray()));
        }
    }

    public static String getDecryptedPwd(String encryptedPwd) {
        String decryptedPwd = "";
        for (int i = encryptedPwd.length() - 1; i >= 0; i--) {
            decryptedPwd += (char) ((int) encryptedPwd.charAt(i) - 1);
        }
        return decryptedPwd;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private static void writeDataToOutputStream(final OutputStream outputStream, final String jsonStringPayload) {
        try (OutputStream os = outputStream) {
            os.write(jsonStringPayload.getBytes());
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDataFromBufferedReader(final InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        String output;
        if (inputStream != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            try {
                while ((output = br.readLine()) != null) {
                    builder.append(output);
                }
            } catch (IOException e) {
                e.printStackTrace();
                builder = new StringBuilder(e.getMessage());
            }
            return builder.toString();
        } else {
            return null;
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
        }
        return httpsURLConnection;
    }

    // Main Function to execute the request as per requirement
    private static JsonObject executeCreateRequest(String step, String method, String url, String requestPayload,
                                                   String contentType, Map<String, String> headers, boolean isReporting) {
        url = url.replace(" ","%20");
        Authenticator.setDefault(new MyAuthenticator());
        HttpURLConnection httpsCon;

        long startTime = Instant.now().toEpochMilli();
        method = method.toUpperCase();

        String requestHeaders = null;

        try {
            URL requestUrl = new URL(url);
            String requestProtocol = requestUrl.getProtocol();

            httpsCon = requestProtocol.equals("https") ? createSSLDisabledHttpsUrlConnection(requestUrl)
                    : (HttpURLConnection) requestUrl.openConnection();

            httpsCon.setRequestProperty("Content-Type", "application/json");
            if (contentType != null && contentType.toLowerCase() != "json") {
                httpsCon.setRequestProperty("Content-Type", "multipart/form-data");
            }

            httpsCon.setDoOutput(true);

            httpsCon.setRequestProperty("accept", "application/json, text/plain, */*");
            httpsCon.setRequestProperty("Connection", "keep-alive");
            httpsCon.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");

            if (headers != null) {
                for (Map.Entry<String, String> set : headers.entrySet()) {
                    httpsCon.setRequestProperty(set.getKey(), set.getValue());
                }

            }

            method = method.toUpperCase();
            switch (method) {
                case "POST": {
                    try {

                        httpsCon.setRequestMethod("POST");
                        httpsCon.setReadTimeout(100000);
                        writeDataToOutputStream(httpsCon.getOutputStream(), requestPayload);
                        break;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                }
                case "PUT": {
                    try {
                        httpsCon.setRequestMethod("PUT");
                        httpsCon.setReadTimeout(100000);
                        writeDataToOutputStream(httpsCon.getOutputStream(), requestPayload);
                        break;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                }
                case "PATCH": {
                    try {

                        httpsCon.setRequestMethod("POST");
                        httpsCon.setRequestProperty("X-HTTP-Method-Override", "PATCH");
                        httpsCon.setReadTimeout(100000);
                        writeDataToOutputStream(httpsCon.getOutputStream(), requestPayload);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                }
                case "GET": {
                    try {
                        httpsCon.setRequestMethod("GET");
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                }
                case "DELETE": {
                    try {
                        httpsCon.setRequestMethod("DELETE");
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }

            requestHeaders = httpsCon.getHeaderFields().toString();
            httpsCon.connect();
            int statusCode = httpsCon.getResponseCode();
            String responseMessage = httpsCon.getResponseMessage();
            String errorMessage = getDataFromBufferedReader(httpsCon.getErrorStream());
            String responseBody = errorMessage == null ? getDataFromBufferedReader(httpsCon.getInputStream()) : null;
            long executionTime = Instant.now().toEpochMilli() - startTime;
            JsonObject responseJSON = new JsonObject();

            responseJSON.addProperty("status", statusCode);
            responseJSON.addProperty("requestHeaders", requestHeaders);
            responseJSON.addProperty("responseMessage", responseMessage);
            responseJSON.add("responseError", errorMessage != null ? JsonParser.parseString(errorMessage) : null);
            responseJSON.add("responseBody", responseBody != null ? JsonParser.parseString(responseBody) : null);
            responseJSON.addProperty("execTime", executionTime + " ms");

            if (!(statusCode >= 200 && statusCode < 300)) {
                responseMessage = errorMessage;
            }

            if (isReporting) {
                GemTestReporter.addTestStep("<b>Request: " + step + "</b>",
                        "<b>Request Url :</b>" + url + "<br> <b>RequestHeaders :</b>" + requestHeaders, STATUS.INFO);

                String description = "<b>Status : </b>" + statusCode + "<br> <b>ResponseMessage : </b>"
                        + responseMessage + "<br> <b>ResponseBody: </b>" + responseBody + "<br> <b>ExecutionTime: </b>"
                        + executionTime + " ms";
                if (statusCode >= 200 && statusCode < 300) {
                    GemTestReporter.addTestStep("<b>Response: " + step + "</b>", description, STATUS.PASS);
                } else {
                    GemTestReporter.addTestStep("<b>Response: " + step + "</b>", description, STATUS.FAIL);
                }
            }
            return responseJSON;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to execute PUT requests
    public static JsonObject putRequest(String url, String requestPayload, String contentType) {
        JsonObject response = executeCreateRequest(null, "PUT", url, requestPayload, contentType, null, false);
        return response;
    }

    public static JsonObject putRequestWithReporting(String step, String url, String requestPayload,
                                                     String contentType) {
        JsonObject response = executeCreateRequest(step, "PUT", url, requestPayload, contentType, null, true);
        return response;
    }

    public static JsonObject putRequest(String url, String requestPayload, String contentType,
                                        Map<String, String> headers) {
        JsonObject response = executeCreateRequest(null, "PUT", url, requestPayload, contentType, headers, false);
        return response;
    }

    public static JsonObject putRequestWithReporting(String step, String url, String requestPayload, String contentType,
                                                     Map<String, String> headers) {
        JsonObject response = executeCreateRequest(step, "PUT", url, requestPayload, contentType, headers, true);
        return response;
    }

    // Method to execute POST Requests
    public static JsonObject postRequest(String url, String requestPayload, String contentType) {
        JsonObject response = executeCreateRequest(null, "POST", url, requestPayload, contentType, null, false);
        return response;
    }

    public static JsonObject postRequestWithReporting(String step, String url, String requestPayload,
                                                      String contentType) {
        JsonObject response = executeCreateRequest(step, "POST", url, requestPayload, contentType, null, true);
        return response;
    }

    public static JsonObject postRequest(String url, String requestPayload, String contentType,
                                         Map<String, String> headers) {
        JsonObject response = executeCreateRequest(null, "POST", url, requestPayload, contentType, headers, false);
        return response;
    }

    public static JsonObject postRequestWithReporting(String step, String url, String requestPayload,
                                                      String contentType, Map<String, String> headers) {
        JsonObject response = executeCreateRequest(step, "POST", url, requestPayload, contentType, headers, true);
        return response;
    }

    // Method to execute Patch requests
    public static JsonObject patchRequest(String url, String requestPayload, String contentType) {
        JsonObject response = executeCreateRequest(null, "PATCH", url, requestPayload, contentType, null, false);
        return response;
    }

    public static JsonObject patchRequestWithReporting(String step, String url, String requestPayload,
                                                       String contentType) {
        JsonObject response = executeCreateRequest(step, "PATCH", url, requestPayload, contentType, null, true);
        return response;
    }

    public static JsonObject patchRequest(String url, String requestPayload, String contentType,
                                          Map<String, String> headers) {
        JsonObject response = executeCreateRequest(null, "PATCH", url, requestPayload, contentType, null, false);
        return response;
    }

    public static JsonObject patchRequestWithReporting(String step, String url, String requestPayload,
                                                       String contentType, Map<String, String> headers) {
        JsonObject response = executeCreateRequest(step, "PATCH", url, requestPayload, contentType, null, true);
        return response;
    }

    // Method to execute GET Request
    public static JsonObject getRequest(String url) {
        JsonObject response = executeCreateRequest(null, "GET", url, null, null, null, false);
        return response;
    }

    public static JsonObject getRequestWithReporting(String step, String url) {
        JsonObject response = executeCreateRequest(step, "GET", url, null, null, null, true);
        return response;
    }

    // Method to execute Delete Request
    public static JsonObject deleteRequest(String url) {
        JsonObject response = executeCreateRequest(null, "Delete", url, null, null, null, false);
        return response;
    }

    public static JsonObject deleteRequestWithReporting(String step, String url) {
        JsonObject response = executeCreateRequest(step, "Delete", url, null, null, null, true);
        return response;
    }

    // Method to execute request for File requestPayload with contentType and
    // headers
    public static JsonObject createRequest(String step, String method, String url, File requestPayload,
                                           String contentType, Map<String, String> headers) {
        StringBuilder payload = new StringBuilder();
        try {
            FileReader fr = new FileReader(requestPayload);
            int i;
            // Holds true till there is nothing to read
            while ((i = fr.read()) != -1) {
                payload.append((char) i);
            }
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        JsonObject responseJson = executeCreateRequest(step, method, url, payload.toString(), contentType, headers,
                false);
        return responseJson;
    }

    // Method to execute request for File requestPayload without contentType
    public static JsonObject createRequest(String method, String url, String requestPayload,
                                           Map<String, String> headers) {
        JsonObject responseJson = executeCreateRequest(null, method, url, requestPayload.toString(), null, headers,
                false);
        return responseJson;
    }

    public static JsonObject createRequestWithReporting(String step, String method, String url, String requestPayload,
                                                        Map<String, String> headers) {
        JsonObject responseJson = executeCreateRequest(step, method, url, requestPayload.toString(), null, headers,
                true);
        return responseJson;
    }

    // Method to execute CreateRequest()
    public static JsonObject CreateRequest(String method, String url, File requestPayload,
                                           Map<String, String> headers) {
        JsonObject responseJson = createRequest(null, method, url, requestPayload, null, headers);
        return responseJson;
    }

    public static JsonObject createRequestWithReporting(String step, String method, String url, File requestPayload,
                                                        Map<String, String> headers) {
        JsonObject responseJson = executeCreateRequest(step, method, url, requestPayload.toString(), null, headers,
                true);
        return responseJson;
    }

    // healthCheck function for JSON file
    @SuppressWarnings("unchecked")
    private static JsonArray healthCheckJson(JsonArray req) {
        JsonArray responseJson = new JsonArray();
        Map<String, JsonElement> responseHashMap = new HashMap<String, JsonElement>();
        for (int i = 0; i < req.size(); i++) {
            JsonObject test = (JsonObject) req.get(i);
            String step = test.get("test_name").getAsString();
            // Start Report
            GemTestReporter.startTestCase(step, "Health Check", false);

            String method = test.get("method").getAsString();
            String url = test.get("endpoint").getAsString();
            if (url.contains("test_response")) {
                url = Jenkins.Replace(url, responseHashMap);
            }
            int expectedStatus = test.get("expected_status").getAsInt();
            String payload = null;
            Map<String, String> headers = new HashMap<String, String>();
            Map<String, String> parameters = new HashMap<String, String>();
            boolean isValidationRequired = false;
            JsonObject validationQueries = null;

            if (test.has("request_body")) {
                payload = String.valueOf(test.get("request_body").getAsJsonObject());
//                if (payload.contains("test_response")) {
//                    payload = Jenkins.Replace(payload, responseHashMap);
//                }
                payload = String.valueOf(VariableReplacement.result(JsonParser.parseString(payload)));
//                GemTestReporter.addTestStep("Payload", payload, STATUS.INFO);
//                payload = test.get("request_body").toString();


            }

            if (test.has("headers")) {
                if (test.get("headers").toString().contains("test_response")) {
                    headers = (Map<String, String>) gson.fromJson(Jenkins.Replace(test.get("headers").toString(), responseHashMap), headers.getClass());
                } else {
                    headers = (Map<String, String>) gson.fromJson(test.get("headers").toString(), headers.getClass());
                }
            }

            if (test.has("parameters")) {
                parameters = (Map<String, String>) gson.fromJson(test.get("parameters").toString(), parameters.getClass());
                if (test.get("parameters").toString().contains("test_response")) {
                    parameters = (Map<String, String>) gson.fromJson(Jenkins.Replace(test.get("parameters").toString(), responseHashMap), parameters.getClass());
                }
                url = ParameterizedUrl.getParameterizedUrl(url, parameters);
            }

            if (test.has("post_validation")) {
                validationQueries = test.get("post_validation").getAsJsonObject();
                if (test.get("post_validation").getAsJsonObject().toString().contains("test_response")) {
                    validationQueries = JsonParser.parseString(Jenkins.Replace(test.get("post_validation").getAsJsonObject().toString(), responseHashMap)).getAsJsonObject();
                }
                isValidationRequired = true;
            }

            try {
                JsonObject response = executeCreateRequest(step, method, url, payload, null, headers, false);
                responseJson.add(response);
                responseHashMap.put("test_response_" + i, response);
                QuanticGlobalVar.globalResponseHM = responseHashMap;
                String executionTime = response.get("execTime").getAsString();
                String requestHeaders = response.get("requestHeaders").getAsString();
                String responseMessage = response.get("responseMessage").getAsString();

                JsonElement responseBody = null;
                if (response.has("responseError") && response.get("responseError").isJsonNull()) {
                    responseBody = response.get("responseBody");
                } else {
                    responseBody = response.get("responseError");
                }
                GemTestReporter.addTestStep("<b>Request: " + step + "</b>",
                        "<b>Request Url :</b>" + url + "<br> <b>RequestHeaders :</b>" + requestHeaders, STATUS.INFO);
                if (!(payload == null)) {
                    GemTestReporter.addTestStep("Payload", payload, STATUS.INFO);
                }
                int actualStatus = response.get("status").getAsInt();
                if (expectedStatus != 0) {
                    String description = "<b>Actual Status: </b>" + actualStatus + "<br> <b>Expected Status: </b>"
                            + expectedStatus + "<br> <b>ResponseMessage : </b>" + responseMessage
                            + "<br> <b>ResponseBody: </b>" + responseBody.toString() + "<br> <b>ExecutionTime: </b>"
                            + executionTime;
                    if (expectedStatus == actualStatus) {
                        GemTestReporter.addTestStep("<b>Response: " + step + "</b>", description, STATUS.PASS);
                    } else {
                        GemTestReporter.addTestStep("<b>Response: " + step + "</b>", description, STATUS.FAIL);
                    }
                } else {

                    if (actualStatus >= 200 && actualStatus < 300) {
                        GemTestReporter.addTestStep("<b>Response: " + step + "</b>", "Status Code: " + actualStatus,
                                STATUS.PASS);
                    } else {
                        GemTestReporter.addTestStep("<b>Response: " + step + "</b>", "Status Code: " + actualStatus,
                                STATUS.FAIL);
                    }
                }

                if (isValidationRequired) {
                    Set<String> keySet = validationQueries.keySet();
                    Iterator keys = keySet.iterator();
//					System.out.println("keySet ---> "+ keySet);
                    while (keys.hasNext()) {
                        String query = keys.next().toString();
                        String targetQuery = validationQueries.get(query).getAsString();
                        //target.trim();
                        String[] targetArray = targetQuery.trim().split("\\s+");
                        int index = targetQuery.indexOf(" ");
                        String operator = targetQuery.substring(0, index);
                        String target = targetQuery.substring(index + 1);
                        if (query.toUpperCase().contains("DEEPSEARCH")) {
                            String deepSearchQuery = query.substring(query.indexOf("(") + 1, query.indexOf(")"));
                            // Call the deepSearch function here with keyname as "deepSearchQuery"
                            JsonArray result = DeepSearch.deepSearch(response.get("responseBody"), deepSearchQuery);
                            if (result.size() == 0) {
                                GemTestReporter.addTestStep("DeepSearch of key ~ " + deepSearchQuery, "DeepSearch Failed <BR> No Such Key Exist in Response", STATUS.FAIL);
                            } else {
//								GemTestReporter.addTestStep("DeepSearch of key ~ '"+deepSearchQuery+"'","DeepSearch Successful <BR>"+result.toString(),STATUS.PASS);
                                Boolean f = false;
                                for (int j = 0; j < result.size(); j++) {
                                    String value = result.get(j).getAsJsonObject().keySet().iterator().next();
                                    String loc = result.get(j).getAsJsonObject().get(value).getAsString();
//									GemTestReporter.addTestStep("DeepSearch of key ~ '"+deepSearchQuery+"'","Value of the Key ~ "+value+"<BR> Location ~ "+loc,STATUS.INFO);
                                    Boolean temp = DeepSearch.assertionMethods(deepSearchQuery, value, target, operator, loc);
                                    if (temp) {
                                        f = temp;
                                    }
                                }
                                if (!f) {
                                    GemTestReporter.addTestStep("DeepSearch of key ~ " + deepSearchQuery, "DeepSearch Failed <BR> Expected value does not match actual value <BR> Expected value ~ " + target, STATUS.FAIL);
                                }
                            }
                        } else {

                            PostAssertion.postAssertion(response.get("responseBody"), query, operator, target);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                GemTestReporter.addTestStep("Some error occurred", "Some error occurred", STATUS.FAIL);
            }
            //end Report
            GemTestReporter.endTestCase();
        }

        return responseJson;
    }

    public static JsonArray healthCheck(JsonArray req) {
        JsonArray responseJson = healthCheckJson(req);
        return responseJson;
    }

    public static JsonArray healthCheck(File requestPayload) {
        StringBuilder payload = new StringBuilder();
        try {
            FileReader fr = new FileReader(requestPayload);
            int i;
            // Holds true till there is nothing to read
            while ((i = fr.read()) != -1) {
                payload.append((char) i);
            }
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        JsonArray req = JsonParser.parseString(payload.toString()).getAsJsonArray();
        JsonArray responseJson = healthCheckJson(req);
        return responseJson;

    }

    public static JsonArray healthCheck(String filePath) {
        File fr = new File(filePath);
        JsonArray responseJson = healthCheck(fr);
        return responseJson;
    }

}