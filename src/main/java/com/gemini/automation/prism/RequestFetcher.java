package com.gemini.automation.prism;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class RequestFetcher implements Runnable {

    APIRequest apiRequest;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public RequestFetcher(APIRequest apiRequest){
        this.apiRequest = apiRequest;
    }

    @Override
    public void run(){
        HttpRequestClient.getResponse(apiRequest).getResponse();
    }

}
