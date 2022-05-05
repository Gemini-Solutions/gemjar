package com.gemini.automation.prism;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


public class JsonHandling {

    public static String getApiData(Object jsonObject){
        try{
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(jsonObject);
        } catch (Exception e){
            System.out.println("not able to print json data");
        }
        return "";
    }

    public static String getDataFromJson(String jObj, String key){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject object = gson.fromJson(jObj, JsonObject.class);
        return object.get(key).getAsString();
    }
}
