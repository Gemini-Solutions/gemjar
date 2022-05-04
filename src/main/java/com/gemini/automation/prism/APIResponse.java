package com.gemini.automation.prism;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class APIResponse {

    private int responseCode = 0;
    private String exceptionType = "";
    private String response = "";
    private String url = "";


    public int getResponseCode() {
        return responseCode;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public String getResponse() {
        Gson gson = new GsonBuilder().create();
        JsonElement je = JsonParser.parseString(response);
        String prettyResponse = gson.toJson(je);
        return prettyResponse;
    }

    public JsonObject getResponseAsJsonObject() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = gson.fromJson(this.response, JsonElement.class);
        return jsonElement.getAsJsonObject();
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "APIResponse{" + "url='" + getUrl() + '\'' +
                ",responseCode=" + getResponseCode() +
                ",exceptionType='" + getExceptionType() + '\'' +
                ",response='" + getResponse() + '\'' + '}';
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
