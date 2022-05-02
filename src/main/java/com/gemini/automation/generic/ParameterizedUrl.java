package com.gemini.automation.generic;

import java.util.Map;
public class ParameterizedUrl {
    public static String getParameterizedUrl(String url, Map<String,String> params){
        String pUrl = url+"?";
        int size = params.size();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            pUrl+=key+"="+value;
            size--;
            if(size!=0){
                pUrl+="&";
            }
        }
        return pUrl;
    }
}