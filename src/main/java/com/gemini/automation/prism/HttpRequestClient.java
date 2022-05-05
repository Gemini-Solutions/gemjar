package com.gemini.automation.prism;


import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class HttpRequestClient {

    public static APIResponse getResponse(APIRequest apiRequest) {
        APIResponse apiResponse = new APIResponse();
        try {
            apiResponse.setUrl(apiRequest.getApiEndPoint());
            RestAssured.baseURI = apiRequest.getApiEndPoint();
            Response resp = RestAssured.given().headers(apiRequest.getRequestProperty()).body(apiRequest.getRequestData()).relaxedHTTPSValidation().when().post();
            apiResponse.setResponseCode(resp.getStatusCode());
            apiResponse.setResponse(resp.body().asString());
            System.out.println(resp.asString());
        } catch (Exception e) {
            System.out.println("Exception in Hitting API");
            e.printStackTrace();
            apiResponse.setExceptionType(e.getClass().getCanonicalName());
        }
        return apiResponse;
    }

    public static APIResponse sendNebulaEmail(final String s_run_id, final String failMail, final String ccMail, final String passMail, final String mail) {
        APIRequest sendEmailRequest = new APIRequest();
        sendEmailRequest.setApiEndPoint("nebmail");//nebula api
        sendEmailRequest.setApiMethod("POST");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("s_run_id", s_run_id);
        jsonObject.addProperty("failmail", failMail);
        jsonObject.addProperty("ccmail", ccMail);
        jsonObject.addProperty("mail", mail);
        jsonObject.addProperty("passmail", passMail);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        sendEmailRequest.setRequestProperty(header);
        sendEmailRequest.setRequestData(jsonObject.toString());
        System.out.println(sendEmailRequest.getRequestData());
        return getResponse(sendEmailRequest);

    }
}
