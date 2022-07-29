package com.gemini.generic;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class GemjarGlobalVar {
    public static String projectName;
    public static String environment;
    public static String reportName;
    public static String testCaseFileName;
    public static String testCaseDataJsonPath;
    public static Properties projectProperty;
    public static Properties GemjarProperty;
    public static List<String> testCasesToRun;
    public static String reportLocation;

    //// Test case variables

    public static String browserInTest;

    //// Mail List

    public static Properties mailingProperty;
    public static String failMail;
    public static String passMail;
    public static String ccMail;
    public static String mail;

    public static String report_type;

    public static Map<String, JsonElement> globalResponseHM;

    public static JsonElement suiteDetail;

    public static String sendMail;

    public static String fromMail = "gemecosystem.gemjar@gmail.com";

    public static String fromMailPwd = "ftgyheqqoqtwvzes";
}
