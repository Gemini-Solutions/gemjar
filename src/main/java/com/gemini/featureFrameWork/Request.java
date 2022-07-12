package com.gemini.featureFrameWork;




import com.gemini.apitest.ApiClientConnect;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cucumber.datatable.DataTable;
import io.cucumber.docstring.DocString;
import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Request {


    private String STEP;
    private  String BASE_URL ;
    private  String PATH;
    private String URL;
    private boolean isURLSet = false;
    private String METHOD;
    private  Map<String, String> PARAMS = new HashMap<String,String>();
    private String parameterString;
    private Map<String,String> headerMap = new HashMap<String,String>();
    private String headerString;

    private JsonElement requestBody;


    public Request(){

    }

    public  String getBaseUrl() {
        return this.BASE_URL;
    }

    public  void setBaseUrl(String baseUrl) {
        this.BASE_URL = baseUrl;
    }

    public  String getPATH() {
        return this.PATH;
    }

    public  void setpath(String path) {
        this.PATH = path;
    }

    public void setURL(){

        if(!isURLSet){try {
            URIBuilder builder = new URIBuilder(this.BASE_URL);
            builder.setPath(this.PATH);

            for (String paramKey:PARAMS.keySet()) {
                builder.setParameter(paramKey,PARAMS.get(paramKey));
            }
            this.URL = builder.build().toURL().toString();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException(e);
        }}

    }

    public void setURL(String url){
            this.URL = url;
            this.isURLSet = true;
    }

    public String getURL(){
      return this.URL;
    }
    public void setParameter(String key, String value) {
        PARAMS.put(key,value);
    }

    public void setMethod(String methodType) {
        this.METHOD = methodType.toLowerCase();
    }

    public String getMethod(){
        return this.METHOD;
    }

    public void createRequest(Object obj) {
        String dataType = obj.getClass().getSimpleName();
        System.out.println(dataType);
        switch (dataType){
            case "DataTable" :
                createRequestFromDataTable((DataTable)obj);
                break;

            case "DocString" :
                createRequestFromDocString((DocString)obj);
                break;
        }
    }

    private void createRequestFromDocString(DocString obj) {
       String data = obj.getContent();
        JsonObject requestData = JsonParser.parseString(data).getAsJsonObject();
        Set<String> providedKeys = requestData.keySet();
        for(String key : providedKeys){
            switch (key){
                case "baseUrl" :
                    setBaseUrl(GemJarUtils.convertJsonElementToString(requestData.get(key)));
                    break;
                case "path" :
                    setpath(GemJarUtils.convertJsonElementToString(requestData.get(key)));
                    break;
                case "url" :
                    setURL(GemJarUtils.convertJsonElementToString(requestData.get(key)));
                    break;
                case "headers" :
                    setHeaders(GemJarUtils.convertJsonElementToString(requestData.get(key)));
                    break;
                case "params" :
                    setParameter(GemJarUtils.convertJsonElementToString(requestData.get(key)));
                    break;
                case "method" :
                    setMethod(GemJarUtils.convertJsonElementToString(requestData.get(key)));
                    break;
                case "requestBody" :
                    setRequestBody(requestData.get(key));
                    break;
                case "expectedStatus" :
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
        if (jsonElement.isJsonPrimitive()){
            setParameter(jsonElement.getAsString());
        } else if (jsonElement.isJsonObject()) {
           Gson gson = new Gson();
           Map<String, String > parameterMap = gson.fromJson(jsonElement,Map.class);
           setParameter(parameterMap);
        }
    }

    public void setParameter(Map<String, String> parameterAsMap) {
    this.PARAMS = parameterAsMap;
    }

    public void setParameter(String paramsAsString) {
        this.parameterString = paramsAsString;
    }


    /*
    Headers
     */
    public void setHeaders(JsonElement jsonElement) {
       if(jsonElement.isJsonPrimitive()){
           setHeaders(jsonElement.getAsString());
       } else if (jsonElement.isJsonObject()) {
           Gson gson = new Gson();
           Map<String,String> headerMap = gson.fromJson(jsonElement, Map.class);
           setHeaders(headerMap);
       }
    }
    public void setHeaders(String headerString) {
        this.headerString = headerString;
    }

    public void setHeaders(Map<String, String> headerKeyValueMap){
        this.headerMap = headerKeyValueMap;
    }

    public void setHeader(String key, String value){
        this.headerMap.put(key,value);
    }


    private void createRequestFromDataTable(DataTable obj) {
    }

    public Response triggerApi(){

        return new Response(ApiClientConnect.executeCreateRequest(this.STEP,
                this.METHOD,
                this.URL,
                this.requestBody !=null ? this.requestBody.toString(): null,
                "json",
                headerMap,
                this.STEP == null ? false : true
                ));
    }
}
