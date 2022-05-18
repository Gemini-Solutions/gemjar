package com.gemini.apitest;

import com.gemini.quartzReporting.GemTestReporter;
import com.gemini.quartzReporting.STATUS;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Iterator;
import java.util.Set;

public class DeepSearch {

    static JsonArray result = new JsonArray();

    public static JsonArray deepSearch(JsonElement request, String key) {
        if (request instanceof JsonArray) {
            deepSearch(request.getAsJsonArray(), key, "");
        } else if (request instanceof JsonObject) {
            deepSearch(request.getAsJsonObject(), key, "");
        }

        return result;
    }

    private static void deepSearch(JsonArray asJsonArray, String key, String s) {//name:[{},{},{},[],[]]
        for (int l = 0; l < asJsonArray.size(); l++) {
            if (asJsonArray.get(l) instanceof JsonObject) {
                deepSearch((JsonObject) asJsonArray.get(l), key, s + "." + l);
            }
            if (asJsonArray.get(l) instanceof JsonArray) {
                deepSearch((JsonArray) asJsonArray.get(l), key, s + "." + l);
            }
        }
    }

    private static void deepSearch(JsonObject obj, String key, String s) {
        Set<?> listt = obj.keySet();
        Iterator<?> i = listt.iterator();
        if (listt.contains(key)) {
//            System.out.println("value = " + obj.get(key).toString());
//            System.out.println("loc = body" + s + "." + key);//{name:[name]}
            JsonObject t = new JsonObject();
            t.addProperty(obj.get(key).getAsString(), "body" + s + "." + key);
            result.add(t);
        }
        do {
            try {
                String value = i.next().toString();
                if (obj.get(value) instanceof JsonObject) {
                    JsonObject subObj = (JsonObject) obj.get(value);
                    deepSearch(subObj, key, s + "." + value);
                } else if (obj.get(value) instanceof JsonArray) {
                    deepSearch((JsonArray) obj.get(value).getAsJsonArray(), key, s + "." + value);
                }
            } catch (Exception e) {
            }
        } while (i.hasNext());
    }

    public static boolean assertionMethods(String key, String temp, String target, String operator, String loc) {
        switch (operator.toUpperCase()) {
            case "TO": {
                if (temp != null && temp.equalsIgnoreCase(target)) {
                    GemTestReporter.addTestStep("DeepSearch Successful of key ~ '" + key + "'", "Expected Value of the Key ~ " + target + "<BR>Actual Value of the Key ~ " + temp + "<BR> Location ~ " + loc, STATUS.PASS);
                    return true;
                } else {
//                    GemTestReporter.addTestStep("DeepSearch Successful of key ~ '" + key + "'", "Expected Value of the Key ~ "+target+"<BR>Actual Value of the Key ~ " + temp + "<BR> Location ~ " + loc, STATUS.FAIL);
                }
                break;
            }

            case "IN": {
                JsonArray targetArray = JsonParser.parseString(target).getAsJsonArray();
//                    System.out.println("JSONARRAY------>" + targetArray);["aka","har"]
                for (int i = 0;i<targetArray.size();i++){
                    if (targetArray.get(i).getAsString().equals(temp)) {
                        GemTestReporter.addTestStep("DeepSearch Successful of key ~ '" + key + "'", "Expected Value of the Key must be in Array ~ " + target + "<BR>Actual Value of the Key ~ " + temp + "<BR> Location ~ " + loc, STATUS.PASS);
                        return true;
                    }
                }

                break;
            }

            case "CONTAINS": {
                if (temp != null && temp.contains(target)) {
                    GemTestReporter.addTestStep("DeepSearch Successful of key ~ '" + key + "'", "Expected Value of the Key must contains ~ " + target + "<BR>Actual Value of the Key ~ " + temp + "<BR> Location ~ " + loc, STATUS.PASS);
                    return true;
                } else {
//                    GemTestReporter.addTestStep("DeepSearch Successful of key ~ '" + key + "'", "Expected Value of the Key must contains ~ "+target+"<BR>Actual Value of the Key ~ " + temp + "<BR> Location ~ " + loc, STATUS.FAIL);
                }
                break;
            }
        }
        return false;
    }
}
