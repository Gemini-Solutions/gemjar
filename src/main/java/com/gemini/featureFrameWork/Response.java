package com.gemini.featureFrameWork;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Response {

    private int Status;
    private String responseMessage;
    private String errorMessage;
    private String responseBody;
    private String execTime;


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

}
