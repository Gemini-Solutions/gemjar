package com.gemini.apitest;

import com.gemini.quartzReporting.GemTestReporter;
import com.gemini.quartzReporting.STATUS;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PostAssertion {
    public static void postAssertion(JsonElement responseBody, String query, String operator, String target) {
        boolean flag = true;
        JsonElement temp = responseBody;
        String tempQuery = query;

        while (flag) {
            int startIndex = query.indexOf("(");
            int endIndex = query.indexOf(")");
            String key = query.substring(startIndex + 1, endIndex);

            temp = PostAssertion.getLeftQuery(temp, key);

            query = query.substring(endIndex + 1);

            if (
                    query.length() <= 0) {
                flag = false;
            }
        }

        try {
            switch (operator.toUpperCase()) {
                case "TO": {
                    if (temp!=null && temp.getAsString().equalsIgnoreCase(target)) {

                        GemTestReporter.addTestStep("Post_Validation","<b>Validation Query: </b>"+ tempQuery + "<br>Expected_Value: "+ target+"<br>Actual_Value: "+ temp.getAsString(), STATUS.PASS);
                    } else {
                        GemTestReporter.addTestStep("Post_Validation","<b>Validation Query: </b>"+ tempQuery + "<br>Expected_Value: "+ target+"<br>Actual_Value: "+ temp.getAsString(), STATUS.FAIL);
                    }
                    break;
                }

                case "IN": {
                    JsonArray targetArray = JsonParser.parseString(target).getAsJsonArray();
//                    System.out.println("JSONARRAY------>" + targetArray);
                    if (targetArray.contains(temp)) {
                        GemTestReporter.addTestStep("Post_Validation","<b>Validation Query: </b>"+ tempQuery + "<br>Expected_Value: "+ target+"<br>Actual_Value: "+ temp.getAsString(), STATUS.PASS);
                    } else {
                        GemTestReporter.addTestStep("Post_Validation","<b>Validation Query: </b>"+ tempQuery + "<br>Expected_Value: "+ target+"<br>Actual_Value: "+ temp.getAsString(), STATUS.FAIL);
                    }
                    break;
                }

                case "CONTAINS": {
                    if (temp!=null && temp.getAsString().toUpperCase().contains(target.toUpperCase())) {
                        GemTestReporter.addTestStep("Post_Validation","<b>Validation Query: </b>"+ tempQuery + "<br>Expected_Value: "+ target+"<br>Actual_Value: "+ temp.getAsString(), STATUS.PASS);
                    } else {
                        GemTestReporter.addTestStep("Post_Validation","<b>Validation Query: </b>"+ tempQuery + "<br>Expected_Value: "+ target+"<br>Actual_Value: "+ temp.getAsString(), STATUS.FAIL);
                    }
                    break;
                }
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private static JsonElement getLeftQuery(JsonElement request, String key){
        boolean isNumeric = key.chars().allMatch( Character::isDigit);
        if(request instanceof JsonArray && isNumeric){
            return request.getAsJsonArray().get(Integer.parseInt(key));
        }
        if(request instanceof JsonObject && !isNumeric){
            return request.getAsJsonObject().get(key);
        }
        return null;
    }
}