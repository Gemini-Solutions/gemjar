package com.gemini.generic.remote.invocation;


import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.utils.URIBuilder;

import com.gemini.generic.feature.framework.GemJarUtils;
import com.gemini.generic.utils.GemUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.cucumber.datatable.DataTable;
import io.cucumber.docstring.DocString;

public class Request {


    private String step;
    private String baseUrl;
    private String path;
    private String url;
    private boolean isURLSet = false;

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    private String method;
    private Map<String, String> params = new HashMap<String, String>();
    private String parameterString;
    private String requestPayload;
    private Map<String, String> headerMap = new HashMap<String, String>();
    private String headerString;
    private String contentType;
    private JsonElement requestBody;

    public Request() {

    }
    public Request(String step, String method, String url, String requestPayload, String contentType, Map<String, String> headers) {
        this.step = step;
        this.method = method;
        this.baseUrl = url;
        this.headerMap = headers;
        this.contentType = contentType;
        this.requestPayload = requestPayload;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(String requestPayload) {
        this.requestPayload = GemUtils.readPayLoad(requestPayload);
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPATH() {
        return this.path;
    }

    public void setpath(String path) {
        this.path = path;
    }

    public void setURL() {

        if (!isURLSet) {
            try {
                URIBuilder builder = new URIBuilder(this.baseUrl);
                builder.setPath(this.path);
                for (String paramKey : params.keySet()) {
                    builder.setParameter(paramKey, params.get(paramKey));
                }
                this.url = builder.build().toURL().toString();
            } catch (URISyntaxException | MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public String getURL() {
        return this.url;
    }

    public void setURL(String url) {
        this.url = url;
        this.isURLSet = true;
    }

    public void setParameter(String key, String value) {
        params.put(key, value);
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String methodType) {
        this.method = methodType.toLowerCase();
    }

    public void createRequest(Object obj) {
        String dataType = obj.getClass().getSimpleName();
        System.out.println(dataType);
        switch (dataType) {
            case "DataTable":
                createRequestFromDataTable((DataTable) obj);
                break;

            case "DocString":
                createRequestFromDocString((DocString) obj);
                break;
        }
    }

    private void createRequestFromDocString(DocString obj) {
        String data = obj.getContent();
        JsonObject requestData = JsonParser.parseString(data).getAsJsonObject();
        Set<String> providedKeys = requestData.keySet();
        for (String key : providedKeys) {
            switch (key) {
                case "baseUrl":
                    setBaseUrl(GemJarUtils.convertJsonElementToString(requestData.get(key)));
                    break;
                case "path":
                    setpath(GemJarUtils.convertJsonElementToString(requestData.get(key)));
                    break;
                case "url":
                    setURL(GemJarUtils.convertJsonElementToString(requestData.get(key)));
                    break;
                case "headers":
                    setHeaders(GemJarUtils.convertJsonElementToString(requestData.get(key)));
                    break;
                case "params":
                    setParameter(GemJarUtils.convertJsonElementToString(requestData.get(key)));
                    break;
                case "method":
                    setMethod(GemJarUtils.convertJsonElementToString(requestData.get(key)));
                    break;
                case "requestBody":
                    setRequestBody(requestData.get(key));
                    break;
                case "expectedStatus":
                    break;
            }
        }
        setURL();
    }

    private void setRequestBody(JsonElement requestBody) {
        this.requestBody = requestBody;
    }

    /*
    Parameter
     */
    public void setParameter(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            setParameter(jsonElement.getAsString());
        } else if (jsonElement.isJsonObject()) {
            Gson gson = new Gson();
            Map<String, String> parameterMap = gson.fromJson(jsonElement, Map.class);
            setParameter(parameterMap);
        }
    }

    public void setParameter(Map<String, String> parameterAsMap) {
        this.params = parameterAsMap;
    }

    public void setParameter(String paramsAsString) {
        this.parameterString = paramsAsString;
    }


    /*
    Headers
     */
    public void setHeaders(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            setHeaders(jsonElement.getAsString());
        } else if (jsonElement.isJsonObject()) {
            Gson gson = new Gson();
            Map<String, String> headerMap = gson.fromJson(jsonElement, Map.class);
            setHeaders(headerMap);
        }
    }

    public void setHeaders(String headerString) {
        this.headerString = headerString;
    }

    public void setHeaders(Map<String, String> headerKeyValueMap) {
        this.headerMap = headerKeyValueMap;
    }

    public void setHeader(String key, String value) {
        this.headerMap.put(key, value);
    }


    private void createRequestFromDataTable(DataTable obj) {
    }


}
