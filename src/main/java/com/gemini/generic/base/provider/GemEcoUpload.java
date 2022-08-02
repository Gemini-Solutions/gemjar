package com.gemini.generic.base.provider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gemini.generic.remote.invocation.ApiRemoteInvocation;
import com.gemini.generic.remote.invocation.Request;
import com.gemini.generic.remote.invocation.Response;
import com.gemini.generic.utils.GemConstants;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GemEcoUpload  {

    static String userName = "akash.garg";
    static String token= "079738be-d85c-4e2d-86fc-518207efb4be1656580812983";

    public static void postNewRecord() {
        JsonElement suite = GemJARGlobalVar.suiteDetail.deepCopy();
        JsonObject payload = (JsonObject) suite.getAsJsonObject().get("Suits_Details");
        payload.remove("Testcase_Info");
        payload.remove("TestCase_Details");
        payload.addProperty("s_id", "test_id");
        String s_report_type = payload.get("report_type").getAsString();
        String a = null;
        payload.addProperty("s_report_type", s_report_type);
        payload.remove("report_type");
        payload.add("miscData", new JsonArray());
//        System.out.println("GemEcoUpload = " + payload.toString());

        Map<String, String> header = new HashMap<String, String>();
        header.put("username", userName);
        header.put("bridgeToken", token);
        Request request = new Request(null, GemConstants.POST, "https://apis.gemecosystem.com/suiteexe", payload.toString(), "json", header);
        try {
            Response response = new ApiRemoteInvocation().handleRequest(request);//        System.out.println("GemEcoupload respone = " + response.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        postStepRecord();

    }

    public static void postStepRecord() {
        JsonElement suite = GemJARGlobalVar.suiteDetail.deepCopy();
        JsonArray testCaseDetails = suite.getAsJsonObject().get("Suits_Details").getAsJsonObject().get("TestCase_Details").getAsJsonArray().deepCopy();
        String s_run_id = suite.getAsJsonObject().get("Suits_Details").getAsJsonObject().get("s_run_id").getAsString();
        for (int i = 0; i < testCaseDetails.size(); i++) {
            JsonObject payload = (JsonObject) testCaseDetails.get(i).deepCopy();
            String n = null;
            payload.addProperty("result_file", n);
            payload.add("miscData", new JsonArray());
            payload.addProperty("s_run_id", s_run_id);
            String tc_run_id = payload.get("tc_run_id").getAsString();
            JsonArray tc_steps = (JsonArray) suite.getAsJsonObject().get("TestStep_Details").getAsJsonObject().get(tc_run_id).getAsJsonObject().get("steps");

            Set<String> tc_step_keys = new HashSet<String>();
            for (int j = 0; j < tc_steps.size(); j++) {
                tc_step_keys = Sets.union(tc_steps.get(j).getAsJsonObject().keySet(), tc_step_keys);
            }

            payload.add("steps", tc_steps);
//            System.out.println("tc = " + payload.toString());
            Map<String, String> header = new HashMap<String, String>();
            header.put("username", userName);
            header.put("bridgeToken", token);
            Request requestNew = new Request(null, GemConstants.POST, "https://apis.gemecosystem.com/testcase", payload.toString(), "json", header);
            try {
                Response response = new ApiRemoteInvocation().handleRequest(requestNew);//            System.out.println("tc respone = " + response.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    }

}
