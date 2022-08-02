package com.gemini.generic.remote.invocation;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gemini.generic.base.provider.GemJARGlobalVar;
import com.gemini.generic.base.provider.ParameterizedUrl;
import com.gemini.generic.quartz.reporting.GemTestReporter;
import com.gemini.generic.quartz.reporting.STATUS;
import com.gemini.generic.utils.GemUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApiRemoteInvocationImpl extends ApiRemoteInvocation {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
                url = ApiHealthCheckUtils.Replace(url, responseHashMap);
            }
            int expectedStatus = test.get("expected_status").getAsInt();
            String payload = null;
            Map<String, String> headers = new HashMap<String, String>();
            Map<String, String> parameters = new HashMap<String, String>();
            boolean isValidationRequired = false;
            JsonObject validationQueries = null;

            if (test.has("request_body")) {
                payload = String.valueOf(test.get("request_body").getAsJsonObject());
                payload = String.valueOf(ApiHealthCheckUtils.result(JsonParser.parseString(payload)));

            }

            if (test.has("headers")) {
                if (test.get("headers").toString().contains("test_response")) {
                    headers = (Map<String, String>) gson.fromJson(ApiHealthCheckUtils.Replace(test.get("headers").toString(), responseHashMap), headers.getClass());
                } else {
                    headers = (Map<String, String>) gson.fromJson(test.get("headers").toString(), headers.getClass());
                }
            }

            if (test.has("parameters")) {
                parameters = (Map<String, String>) gson.fromJson(test.get("parameters").toString(), parameters.getClass());
                if (test.get("parameters").toString().contains("test_response")) {
                    parameters = (Map<String, String>) gson.fromJson(ApiHealthCheckUtils.Replace(test.get("parameters").toString(), responseHashMap), parameters.getClass());
                }
                url = ParameterizedUrl.getParameterizedUrl(url, parameters);
            }

            if (test.has("post_validation")) {
                validationQueries = test.get("post_validation").getAsJsonObject();
                if (test.get("post_validation").getAsJsonObject().toString().contains("test_response")) {
                    validationQueries = JsonParser.parseString(ApiHealthCheckUtils.Replace(test.get("post_validation").getAsJsonObject().toString(), responseHashMap)).getAsJsonObject();
                }
                isValidationRequired = true;
            }

            GemTestReporter.addTestStep("<b>Request: " + step + "</b>",
                    "<b>Request Url :</b>" + url + "<br> <b>RequestHeaders :</b>" + headers, STATUS.INFO);
            if (!(payload == null)) {
                GemTestReporter.addTestStep("Payload", payload, STATUS.INFO);
            }

            try {
                Response response = GemUtils.invokeRequestMethod(new Request(step, method, url, payload, null, headers));
                responseJson.add(response.getJsonObject());
                responseHashMap.put("test_response_" + i, response.getJsonObject());
                GemJARGlobalVar.globalResponseHM = responseHashMap;
                String executionTime = response.getExecTime();
                String requestHeaders = response.getRequestHeaders();
                String responseMessage = null;
                responseMessage = response.getResponseMessage();
                if (!(response.getStatus() >= 200 && response.getStatus() < 300)) {
                    responseMessage = response.getErrorMessage();
                }
                int actualStatus = response.getStatus();
                if (expectedStatus != 0) {
                    String description = "<b>Actual Status: </b>" + actualStatus + "<br> <b>Expected Status: </b>"
                            + expectedStatus + "<br> <b>ResponseMessage : </b>" + responseMessage
                            + "<br> <b>ExecutionTime: </b>"
                            + executionTime+ "<br> <b>ResponseBody: </b>" + response.getResponseBody() ;
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
                            JsonArray result = ApiHealthCheckUtils.deepSearch(JsonParser.parseString(response.getResponseBody()), deepSearchQuery);
                            if (result.size() == 0) {
                                GemTestReporter.addTestStep("DeepSearch of key ~ " + deepSearchQuery, "DeepSearch Failed <BR> No Such Key Exist in Response", STATUS.FAIL);
                            } else {
                                Boolean f = false;
                                for (int j = 0; j < result.size(); j++) {
                                    String value = result.get(j).getAsJsonObject().keySet().iterator().next();
                                    String loc = result.get(j).getAsJsonObject().get(value).getAsString();
//									GemTestReporter.addTestStep("DeepSearch of key ~ '"+deepSearchQuery+"'","Value of the Key ~ "+value+"<BR> Location ~ "+loc,STATUS.INFO);
                                    Boolean temp = ApiHealthCheckUtils.assertionMethods(deepSearchQuery, value, target, operator, loc);
                                    if (temp) {
                                        f = temp;
                                    }
                                }
                                if (!f) {
                                    GemTestReporter.addTestStep("DeepSearch of key ~ " + deepSearchQuery, "DeepSearch Failed <BR> Expected value does not match actual value <BR> Expected value ~ " + target, STATUS.FAIL);
                                }
                            }
                        } else {

                            ApiHealthCheckUtils.postAssertion(JsonParser.parseString(response.getResponseBody()), query, operator, target);
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

    private static JsonArray healthCheck(JsonArray req) {
        JsonArray responseJson = healthCheckJson(req);
        return responseJson;
    }


    private static JsonArray healthCheckJsonWithoutNewTC(JsonArray req) {
        JsonArray responseJson = new JsonArray();
        Map<String, JsonElement> responseHashMap = new HashMap<String, JsonElement>();
        for (int i = 0; i < req.size(); i++) {
            JsonObject test = (JsonObject) req.get(i);
            String step = test.get("test_name").getAsString();
            String method = test.get("method").getAsString();
            String url = test.get("endpoint").getAsString();
            if (url.contains("test_response")) {
                url = ApiHealthCheckUtils.Replace(url, responseHashMap);
            }
            int expectedStatus = test.get("expected_status").getAsInt();
            String payload = null;
            Map<String, String> headers = new HashMap<String, String>();
            Map<String, String> parameters = new HashMap<String, String>();
            boolean isValidationRequired = false;
            JsonObject validationQueries = null;

            if (test.has("request_body")) {
                payload = String.valueOf(test.get("request_body").getAsJsonObject());
                payload = String.valueOf(ApiHealthCheckUtils.result(JsonParser.parseString(payload)));
            }

            if (test.has("headers")) {
                if (test.get("headers").toString().contains("test_response")) {
                    headers = (Map<String, String>) gson.fromJson(ApiHealthCheckUtils.Replace(test.get("headers").toString(), responseHashMap), headers.getClass());
                } else {
                    headers = (Map<String, String>) gson.fromJson(test.get("headers").toString(), headers.getClass());
                }
            }

            if (test.has("parameters")) {
                parameters = (Map<String, String>) gson.fromJson(test.get("parameters").toString(), parameters.getClass());
                if (test.get("parameters").toString().contains("test_response")) {
                    parameters = (Map<String, String>) gson.fromJson(ApiHealthCheckUtils.Replace(test.get("parameters").toString(), responseHashMap), parameters.getClass());
                }
                url = ParameterizedUrl.getParameterizedUrl(url, parameters);
            }

            if (test.has("post_validation")) {
                validationQueries = test.get("post_validation").getAsJsonObject();
                if (test.get("post_validation").getAsJsonObject().toString().contains("test_response")) {
                    validationQueries = JsonParser.parseString(ApiHealthCheckUtils.Replace(test.get("post_validation").getAsJsonObject().toString(), responseHashMap)).getAsJsonObject();
                }
                isValidationRequired = true;
            }

            GemTestReporter.addTestStep("<b>Request: " + step + "</b>",
                    "<b>Request Url :</b>" + url + "<br> <b>RequestHeaders :</b>" + headers, STATUS.INFO);
            if (!(payload == null)) {
                GemTestReporter.addTestStep("Payload", payload, STATUS.INFO);
            }

            try {
                Response response = GemUtils.invokeRequestMethod(new Request(step, method, url, payload, null, headers));
                responseJson.add(response.getJsonObject());
                responseHashMap.put("test_response_" + i, response.getJsonObject());
                GemJARGlobalVar.globalResponseHM = responseHashMap;
                String executionTime = response.getExecTime();
                String requestHeaders = response.getRequestHeaders();
                String responseMessage = null;
                responseMessage = response.getResponseMessage();
                if (!(response.getStatus() >= 200 && response.getStatus() < 300)) {
                    responseMessage = response.getErrorMessage();
                }
                int actualStatus = response.getStatus();
                if (expectedStatus != 0) {
                    String description = "<b>Actual Status: </b>" + actualStatus + "<br> <b>Expected Status: </b>"
                            + expectedStatus + "<br> <b>ResponseMessage : </b>" + responseMessage
                            + "<br> <b>ExecutionTime: </b>"
                            + executionTime+ "<br> <b>ResponseBody: </b>" + response.getResponseBody() ;
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
                            JsonArray result = ApiHealthCheckUtils.deepSearch(JsonParser.parseString(response.getResponseBody()), deepSearchQuery);
                            if (result.size() == 0) {
                                GemTestReporter.addTestStep("DeepSearch of key ~ " + deepSearchQuery, "DeepSearch Failed <BR> No Such Key Exist in Response", STATUS.FAIL);
                            } else {
                                Boolean f = false;
                                for (int j = 0; j < result.size(); j++) {
                                    String value = result.get(j).getAsJsonObject().keySet().iterator().next();
                                    String loc = result.get(j).getAsJsonObject().get(value).getAsString();
//									GemTestReporter.addTestStep("DeepSearch of key ~ '"+deepSearchQuery+"'","Value of the Key ~ "+value+"<BR> Location ~ "+loc,STATUS.INFO);
                                    Boolean temp = ApiHealthCheckUtils.assertionMethods(deepSearchQuery, value, target, operator, loc);
                                    if (temp) {
                                        f = temp;
                                    }
                                }
                                if (!f) {
                                    GemTestReporter.addTestStep("DeepSearch of key ~ " + deepSearchQuery, "DeepSearch Failed <BR> Expected value does not match actual value <BR> Expected value ~ " + target, STATUS.FAIL);
                                }
                            }
                        } else {

                            ApiHealthCheckUtils.postAssertion(JsonParser.parseString(response.getResponseBody()), query, operator, target);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                GemTestReporter.addTestStep("Some error occurred", "Some error occurred", STATUS.FAIL);
            }
            //end Report
//            GemTestReporter.endTestCase();
        }

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
    //Method to Do Reporting
    public static void doReporting(Response responseJSON, Request request) {
        int statusCode = responseJSON.getStatus();
        String step = request.getStep();
        String requestHeaders = responseJSON.getRequestHeaders();
        String url = request.getURL();
        String responseMessage = responseJSON.getResponseMessage();
        String responseBody = responseJSON.getResponseBody();
        String executionTime = responseJSON.getExecTime();
        if (!(responseJSON.getStatus() >= 200 && responseJSON.getStatus() < 300)) {
            responseMessage = responseJSON.getErrorMessage();
        }
        GemTestReporter.addTestStep("<b>Request: " + step + "</b>", "<b>Request Url :</b>" + url + "<br> <b>RequestHeaders :</b>" + requestHeaders, STATUS.INFO);
        String description = "<b>Status : </b>" + statusCode + "<br> <b>ResponseMessage : </b>" + responseMessage + "<br> <b>ExecutionTime: </b>" + executionTime + " ms <br>" + " <b>ResponseBody: </b>" + responseBody;
        if (statusCode >= 200 && statusCode < 300) {
            GemTestReporter.addTestStep("<b>Response: " + step + "</b>", description, STATUS.PASS);
        } else {
            GemTestReporter.addTestStep("<b>Response: " + step + "</b>", description, STATUS.FAIL);
        }
    }
}
