package com.gemini.generic.remote.invocation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Instant;

import org.apache.log4j.Logger;

import com.gemini.generic.utils.GemUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Response {

    private final static Logger logger = Logger.getLogger(Response.class);

    private int Status;
    private String responseMessage;
    private String errorMessage;
    private String responseBody;
    private String execTime;

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    private String requestHeaders;

    private JsonObject jsonObject;


    public Response(){

    }
    public Response(JsonElement responseData){
        JsonObject response = responseData.getAsJsonObject();
        this.Status = response.get("status").getAsInt();
        this.responseMessage = response.get("responseMessage").getAsString();
        this.responseBody = response.get("responseBody") !=null ? response.get("responseBody").toString(): null;
        this.errorMessage = response.get("responseError") !=null ?  response.get("responseError").toString() : null;
        this.execTime = response.get("execTime") != null ? response.get("execTime").getAsString() : null;
    }

    public Response(HttpURLConnection httpsCon,long startTime,String requestHeaders){
        try {
            this.Status = httpsCon.getResponseCode();
            this.responseMessage = httpsCon.getResponseMessage();
            this.errorMessage = GemUtils.getDataFromBufferedReader(httpsCon.getErrorStream());
            this.responseBody = this.errorMessage == null ? GemUtils.getDataFromBufferedReader(httpsCon.getInputStream()) : null;
            this.execTime=  String.valueOf(Instant.now().toEpochMilli() - startTime);
            this.requestHeaders = requestHeaders;
        } catch (IOException e) {
            logger.info("I/O Exception Occured while creating response Constructor");
            throw new RuntimeException(e);
        }
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int status){
        this.Status = status;
    }
    public void setResponseMessage(String responseMessage){
        this.responseMessage = responseMessage;
    }
    public String getResponseMessage(){
        return this.responseMessage;
    }
    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }

    public String getResponseBody(){
        return this.responseBody;
    }

    public void setResponseBody(String responseBody){
        this.responseBody = responseBody;
    }
    public void setExecTime(String responseTimeInMilliSec){
        this.execTime = responseTimeInMilliSec;
    }
    public String getExecTime(){
        return this.execTime;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public  void setJsonObject() {
        jsonObject.addProperty("status", this.getStatus());
        jsonObject.addProperty("requestHeaders", requestHeaders);
        jsonObject.addProperty("responseMessage", this.getResponseMessage());
        jsonObject.add("responseError", this.getErrorMessage() != null ? JsonParser.parseString(this.getErrorMessage()) : null);
        jsonObject.add("responseBody", this.getResponseBody() != null ? JsonParser.parseString(this.getResponseBody()) : null);
        jsonObject.addProperty("execTime", this.getExecTime() + " ms");
    }
}
