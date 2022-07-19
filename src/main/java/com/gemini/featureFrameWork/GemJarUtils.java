package com.gemini.featureFrameWork;

import com.gemini.generic.GemJARGlobalVar;
import com.gemini.generic.TestCaseData;
import com.gemini.listners.PropertyListeners;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.gemini.generic.QuanticGenericUtils.getTestCaseFileName;
import static com.gemini.generic.QuanticGenericUtils.getTestCasesToRunFromSystemProperties;

public class GemJarUtils extends GemJARGlobalVar {

    public static final String ENVIRONMENT = "environment";
    public static final String REPORTNAME = "reportName";
    public static final String REPORTLOCATION = "reportLocation";
    public static final String BROWSERNAME = "browserName";
    public static final String BROWSERTIMEOUT = "browserTimeout";
    public static final String LAUNCHURL = "launchURL";
    public static final String BASEURL = "baseURL";


    public static final String TESTCASEFILENAME = "testCaseFileName";

    public static JsonObject configJsonObject = new JsonObject();

    public static void loadGemJarConfigData() {
        try {
            String configData = IOUtils.toString(ClassLoader.getSystemResourceAsStream("gemjar-config.json"), Charset.defaultCharset());
            JsonElement configJsonElement = JsonParser.parseString(configData);
            configJsonObject = configJsonElement.getAsJsonObject();
            initializeGemJARGlobalVariables();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonObject getConfigObject() {
        return GemJarUtils.configJsonObject;
    }

    protected static JsonElement getEnvironmentBasedValue(String key) {
        JsonObject jsonObject = configJsonObject;
        JsonElement environmentData = jsonObject.get(GemJARGlobalVar.environment) != null ?
                jsonObject.get(GemJARGlobalVar.environment) : null;
        return environmentData != null ?
                environmentData.getAsJsonObject().get(key) : null;
    }

    public static JsonElement getGemJarConfigData(String key) {
        JsonElement valueFromEnvironment = getEnvironmentBasedValue(key);
        if (valueFromEnvironment != null) {
            return valueFromEnvironment;
        } else if (configJsonObject.get(key) != null) {
            return configJsonObject.get(key);
        } else {
            return null;
        }
    }


    public static String getProjectName() {
        try {
            String sysPropProjectName = System.getProperty("projectName");
            String mavenProjectName = GemJARGlobalVar.gemJARProperties.getProperty("artifactId");
            String projectName = sysPropProjectName != null ? sysPropProjectName
                    : mavenProjectName != null ? mavenProjectName : null;
            return projectName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getProjectEnvironment() {
        String sysPropEnvironment = System.getProperty("projectEnvironment");
        String environmentFromConfig = convertJsonElementToString(configJsonObject.get(ENVIRONMENT));
        String environment = sysPropEnvironment != null ? sysPropEnvironment
                : environmentFromConfig != null ? environmentFromConfig : "beta";
        return environment;
    }

    public static String convertJsonElementToString(JsonElement jsonElement) {

        return jsonElement!= null? ( jsonElement.isJsonNull()  ?
                null : jsonElement.isJsonPrimitive() ?
                jsonElement.getAsJsonPrimitive().getAsString() : jsonElement.toString()):null;

    }

    public static String getProjectReportName() {
        String sysPropReportName = System.getProperty("reportName");
        String reportNameFromConfig = null;
        try {
            reportNameFromConfig = convertJsonElementToString(getGemJarConfigData(REPORTNAME));
        } catch (Exception e) {
            reportNameFromConfig = null;
        }
        String reportName = sysPropReportName != null ? sysPropReportName
                : reportNameFromConfig != null ? reportNameFromConfig
                : GemJARGlobalVar.projectName + " Test Report";
        return reportName;
    }

    public static void initializeGemJARGlobalVariables() {

        GemJARGlobalVar.gemJARProperties = PropertyListeners
                .loadProjectProperties(ClassLoader.getSystemResourceAsStream("gemjar.properties"));
        GemJARGlobalVar.projectName = getProjectName();
        GemJARGlobalVar.environment = getProjectEnvironment();
        GemJARGlobalVar.reportName = getProjectReportName();
        GemJARGlobalVar.reportLocation = getReportLocation();
        if(GemJARGlobalVar.report_type.equalsIgnoreCase("UI Automation")) {
            GemJARGlobalVar.browserInTest = getBrowserToTest();
        }
        ////
        GemJARGlobalVar.testCaseFileName = getTestCaseFileName();
        GemJARGlobalVar.testCaseDataJsonPath = System.getProperty("QuanticTestCaseDataJsonPath");
        GemJARGlobalVar.testCasesToRun = getTestCasesToRunFromSystemProperties();
        TestCaseData.setProjectTestCaseData(ClassLoader.getSystemResourceAsStream(GemJARGlobalVar.testCaseFileName));

    }

    private static String getReportLocation() {
        try {
            String systemReportLocation = System.getProperty(REPORTLOCATION);
            String reportLocationFromConfig = null;
            try {
                reportLocationFromConfig = convertJsonElementToString(getGemJarConfigData(REPORTLOCATION));

            } catch (Exception e) {
            }
            String loc = systemReportLocation != null && !systemReportLocation.isEmpty()
                    ? systemReportLocation : reportLocationFromConfig != null && !reportLocationFromConfig.isEmpty() ?
                    reportLocationFromConfig : System.getProperty("user.dir");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
            DateTimeFormatter hms = DateTimeFormatter.ofPattern("HHmmss");
            loc = loc + "/Report/" + dtf.format(now) + "/" + hms.format(now);
            return loc;
        } catch (Exception e) {
            System.out.println("Some Error Occur With reportLocation . Default reportLocation Set");
            return "";
        }
    }


    public static String getBrowserToTest() {
        String browserName = System.getProperty(BROWSERNAME);
        String browserNameFromConfig =null;
        try{
            browserNameFromConfig =convertJsonElementToString(getGemJarConfigData(BROWSERNAME));
        }catch (Exception e){
            browserNameFromConfig =null;
        }
        String browser = browserName != null ? browserName
                : browserNameFromConfig != null ? browserNameFromConfig : "chrome";
        return browser;
    }


}
