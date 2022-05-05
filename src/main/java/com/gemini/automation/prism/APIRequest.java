package com.gemini.automation.prism;

import java.util.HashMap;
import java.util.Map;

public class APIRequest {
    private String apiEndPoint = "";
    private String apiMethod = "";
    private String requestData = "";
    private Map<String, String> requestProperty;

    public String getApiEndPoint() {
        return apiEndPoint;
    }

    public APIRequest(String requestData) {
        this.apiEndPoint = "https://www.google.com/";
        this.apiMethod = "GET";
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        this.requestProperty = header;
        this.requestData = requestData;
    }

    public APIRequest() {

    }

    @Override
    public String toString() {
        return "APIRequest{apiEndPoint='" + apiEndPoint + '\'' + ",apiMethod='" + apiMethod + '\'' + ",requestData='" + requestData + '\'' + ",requestProperty=" + requestProperty + '}';

    }

    public void setApiEndPoint(String apiEndPoint) {
        this.apiEndPoint = apiEndPoint;
    }

    public String getApiMethod(){return apiMethod;}

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getRequestData(){return requestData;}

    public void setRequestProperty(Map<String, String> requestProperty) {
        this.requestProperty = requestProperty;
    }


    public Map<String,String> getRequestProperty(){return requestProperty;}
}
