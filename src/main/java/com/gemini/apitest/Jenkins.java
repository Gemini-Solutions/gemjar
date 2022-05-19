package com.gemini.apitest;

import com.google.gson.JsonElement;

import java.util.Map;

public class Jenkins {

    public static String Replace(String value, Map hashMap) {
        while(value.contains("#")){
            int startIndex = value.indexOf("#");//akash#garg#
            int endIndex = value.substring(startIndex + 1).indexOf("#") + startIndex + 1;
            String key = value.substring(startIndex+1,endIndex);
//           System.out.println("key = "+value.substring(startIndex,endIndex));
            String target = "";
//            test_response[1](name)
            int startIndexBracket = key.indexOf("[");
            int endIndexBacket = key.indexOf("]");
            int bracketValue = Integer.parseInt(key.substring(startIndexBracket+1,endIndexBacket));
            JsonElement loc = (JsonElement) hashMap.get("test_response_"+bracketValue);
            String query = value.substring(startIndex+endIndexBacket+2,endIndex);
            target = PostAssertion.postAssertion(loc.getAsJsonObject().get("responseBody"),query);
            value = value.replace("#"+key+"#",target);
//            System.out.println(value);
        }
        return value;
    }
}
