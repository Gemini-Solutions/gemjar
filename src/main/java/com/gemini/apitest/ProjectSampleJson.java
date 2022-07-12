package com.gemini.apitest;

import com.gemini.generic.ProjectProperties;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

public class ProjectSampleJson {
    private static HashMap<String, JsonElement> sampleJsonObjectMap = new HashMap<String, JsonElement>();
    private static HashMap<String, String> sampleJsonObjectStringMap = new HashMap<String, String>();

    public static void loadSampleJson() {
        Set<String> projectPropertyKeys = ProjectProperties.getStringPropertyNames();
        for (String key : projectPropertyKeys) {
            if (key.toLowerCase().contains("_samplejson")) {
                String jsonPath = ProjectProperties.getProperty(key);
                InputStream jsonInputStream = ClassLoader.getSystemResourceAsStream(jsonPath);
                InputStreamReader jsonReader = new InputStreamReader(jsonInputStream);
                JsonElement sampleJsonDataJsonElement = JsonParser.parseReader(jsonReader);
                String sampleJsonDataString = sampleJsonDataJsonElement.toString();

                sampleJsonObjectMap.put(key, sampleJsonDataJsonElement);
                sampleJsonObjectStringMap.put(key, sampleJsonDataString);
            }
        }
    }

    public static JsonElement getSampleData(String sampleName) {
        if (sampleJsonObjectMap.containsKey(sampleName)) {
            return sampleJsonObjectMap.get(sampleName);
        } else {
            return null;
        }
    }

    public static String getSampleDataString(String sampleName) {
        if (sampleJsonObjectStringMap.containsKey(sampleName)) {
            return sampleJsonObjectStringMap.get(sampleName);
        } else {
            return null;
        }
    }

    public static void addSampleData(String sampleName, String sampleValue) {
        try {
            sampleJsonObjectMap.put(sampleName, JsonParser.parseString(sampleValue));
            sampleJsonObjectStringMap.put(sampleName, sampleValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addSampleData(String sampleName, JsonElement sampleValue) {
        try {
            sampleJsonObjectMap.put(sampleName, sampleValue);
            sampleJsonObjectStringMap.put(sampleName, sampleValue.getAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
