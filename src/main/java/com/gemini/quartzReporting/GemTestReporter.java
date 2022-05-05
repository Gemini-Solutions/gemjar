package com.gemini.quartzReporting;

import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;

public class GemTestReporter {


    private static ThreadLocal<TestCase_Details> testCase_Details = new ThreadLocal<TestCase_Details>();
    private static JsonObject stepJson = new JsonObject();
    private static ThreadLocal<JsonArray> steps = new ThreadLocal<JsonArray>();
    private static volatile Suits_Details suiteDetails;
    private static QuartzReporting reporting;
    public static String ReportLocation = null;


    public static void startSuite(String projectName, String env, String reportLocation) {
        ReportLocation = reportLocation;
        String s_run_id = projectName + "_" + GemReportingUtility.getCurrentTimeInMilliSecond() + "_" + env.toUpperCase();
        suiteDetails = new Suits_Details(s_run_id, projectName, env);
        reporting = new QuartzReporting(suiteDetails);
    }

    public static void startSuite(String projectName, String env) {
        ReportLocation = null;
        String s_run_id = projectName + "_" + GemReportingUtility.getCurrentTimeInMilliSecond() + "_" + env.toUpperCase();
        suiteDetails = new Suits_Details(s_run_id, projectName, env);
        reporting = new QuartzReporting(suiteDetails);
    }

    public static void startTestCase(String testcaseName, String category, String productType, boolean ignore) {
        steps.set(new JsonArray());
        testCase_Details.set(new TestCase_Details(testcaseName, category, GemReportingUtility.getCurrentUserName(), productType, ignore));
    }


    public static void addTestStep(String stepTitle, String stepDescription, STATUS status) {
        addTestStep(stepTitle, stepDescription, status, new HashMap<String, String>());
    }

    public static void addTestStep(String stepTitle, String stepDescription, STATUS status, String screenShotPath) {
        Map<String, String> scrnshot = new HashMap<String, String>();
        boolean isBase64 = Base64.isArrayByteBase64(screenShotPath.getBytes());
        if (isBase64) {
            scrnshot.put("ScreenShot", "data:image/gif;base64, " + screenShotPath);
        } else {
            scrnshot.put("ScreenShot", screenShotPath);
        }
        addTestStep(stepTitle, stepDescription, status, scrnshot);
    }

    public static void addTestStep(String stepTitle, String stepDescription, STATUS status, Map<String, String> extraKeys) {
        JsonObject step = new JsonObject();
        step.addProperty("title", "<b>" + stepTitle + "</b>");
        step.addProperty("description", stepDescription);
        step.addProperty("status", status.name());
        if (extraKeys != null) {
            Set<String> extraKeySet = extraKeys.keySet();
            for (String key : extraKeySet) {
                step.addProperty(key, extraKeys.get(key));
            }
        }
        steps.get().add(step);
    }


    public synchronized static void endTestCase() {
        testCase_Details.get().setStatus(steps.get());
        testCase_Details.get().endTestCase();
        System.out.println(testCase_Details.get().toString());
        suiteDetails.addTestCaseDetail(testCase_Details.get());


        String testCaseRunID = testCase_Details.get().getTc_run_id();

        JsonObject testCaseStep = new JsonObject();
        testCaseStep.add("steps", steps.get());
        testCaseStep.add("metaData", createTestCaseMetaData());
        stepJson.add(testCaseRunID, testCaseStep);
    }

    private static JsonArray createTestCaseMetaData() {
        JsonArray metaData = new JsonArray();
        JsonObject testcaseName = new JsonObject();
        testcaseName.addProperty("TESTCASE NAME", testCase_Details.get().getName());
        testcaseName.addProperty("SERVICE PROJECT", "");
        JsonObject dateOfExecution = new JsonObject();
        dateOfExecution.addProperty("value", testCase_Details.get().getStart_time());
        dateOfExecution.addProperty("type", "date");
        testcaseName.add("DATE OF EXECUTION", dateOfExecution);
        metaData.add(testcaseName);

        JsonObject executionTimeDetail = new JsonObject();
        JsonObject startTimeDetail = new JsonObject();
        startTimeDetail.addProperty("value", testCase_Details.get().getStart_time());
        startTimeDetail.addProperty("type", "date");
        executionTimeDetail.add("EXECUTION STARTED ON", startTimeDetail);

        JsonObject endTimeDetail = new JsonObject();
        endTimeDetail.addProperty("value", testCase_Details.get().getEnd_time());
        endTimeDetail.addProperty("type", "date");
        executionTimeDetail.add("EXECUTION ENDED ON", endTimeDetail);

        executionTimeDetail.addProperty("EXECUTION DURATION",
                (testCase_Details.get().getEnd_time() - testCase_Details.get().getStart_time()) / 1000 + " seconds");
        metaData.add(executionTimeDetail);
        metaData.add(getStepStats());

        return metaData;
    }

    private static JsonObject getStepStats() {
        JsonObject stepStats = new JsonObject();
        stepStats.addProperty("TOTAL", steps.get().size());
        Map<String, Integer> statMap = new HashMap<String, Integer>();

        for (JsonElement step : steps.get()) {
            String statusName = step.getAsJsonObject().get("status").getAsString();
            if (statMap.get(statusName) == null) {
                statMap.put(statusName, 1);
            } else {
                statMap.put(statusName, statMap.get(statusName) + 1);
            }
        }
        for (String statusKey : statMap.keySet()) {
            stepStats.addProperty(statusKey, statMap.get(statusKey));
        }

        return stepStats;

    }

    public static void endSuite() {
        suiteDetails.endSuite();
        createReport();

    }

    private static void createReport() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //String suiteDetail = gson.toJson(reporting, QuartzReporting.class);
        JsonElement suiteDetail = gson.toJsonTree(reporting);
        suiteDetail.getAsJsonObject().add("TestStep_Details", stepJson);
        //remove empty Val
        String[] status = new String[]{"INFO", "WARN", "INCOMPLETE", "EXE", "REQ"};
        for (String s : status) {
            if (suiteDetail.getAsJsonObject().get("Suits_Details").getAsJsonObject().get("Testcase_Info").getAsJsonObject().get(s).getAsInt() == 0) {
                suiteDetail.getAsJsonObject().get("Suits_Details").getAsJsonObject().get("Testcase_Info").getAsJsonObject().remove(s);
            }
        }
        System.out.println("SuitDetails " + suiteDetail.toString());
//        System.out.println("----------------------------------------------------------------");
//        System.out.println("testCaseDetails"+stepJson.toString());
        try {
            GemReportingUtility.createReport(suiteDetail.toString(), stepJson.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static JsonElement getSuiteDetails() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement suiteDetail = gson.toJsonTree(reporting);
        return suiteDetail;
    }

    public static String getTestStepDetails() {
        String testStepJson = stepJson.toString();
        return testStepJson;
    }
}