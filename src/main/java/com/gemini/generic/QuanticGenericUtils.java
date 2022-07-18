package com.gemini.generic;

import com.gemini.listners.PropertyListeners;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.gemini.featureFrameWork.GemJarUtils.convertJsonElementToString;
import static com.gemini.featureFrameWork.GemJarUtils.getGemJarConfigData;

public class QuanticGenericUtils extends GemJARGlobalVar {
    public static void setKerberosRequiredConfiguration() {
        try {
            System.setProperty("java.security.krb5.conf",
                    new File(ClassLoader.getSystemResource("krb5.conf").toURI()).getAbsolutePath());
            System.setProperty("java.security.auth.login.config",
                    new File(ClassLoader.getSystemResource("login.conf").toURI()).getAbsolutePath());
            System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
            System.setProperty("com.sun.net.ssl.checkRevocation", "false");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProjectName() {
        try {
            String sysPropProjectName = System.getProperty("QuanticProjectName");
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
        String sysPropEnvironment = System.getProperty("QuanticProjectEnvironment");
        String environmentFromPropertiesFile = GemJARGlobalVar.projectProperty.getProperty("environment");
        String environment = sysPropEnvironment != null ? sysPropEnvironment
                : environmentFromPropertiesFile != null ? environmentFromPropertiesFile : "beta";
        return environment;
    }

    public static String getProjectReportName() {
        String sysPropReportName = System.getProperty("QuanticReportName");
        String reportNameFromPropFiles = convertJsonElementToString(getGemJarConfigData("reportName"));
        String reportName = sysPropReportName != null ? sysPropReportName
                : reportNameFromPropFiles != null ? reportNameFromPropFiles
                : GemJARGlobalVar.projectName + " Test report";
        return reportName;
    }

    public static String getTestCaseFileName() {
        String sysTestCaseFileName = System.getProperty("QuanticTestCaseFileName");
        String testCaseFileNameFromProjProp = null;
        try {
            testCaseFileNameFromProjProp = convertJsonElementToString(getGemJarConfigData("testCaseFileName"));
        } catch (Exception e) {
            testCaseFileNameFromProjProp = null;
        }
        String testCaseFileName = sysTestCaseFileName != null ? sysTestCaseFileName
                : testCaseFileNameFromProjProp != null ? testCaseFileNameFromProjProp
                : GemJARGlobalVar.projectName + "_testCase.json";
        return testCaseFileName;
    }

    public static List<String> getTestCasesToRunFromSystemProperties() {
        List<String> testCasesToRun;
        String testCaseString = System.getProperty("QuanticTestCasesToRun");
        String[] testCaseArray = testCaseString != null ? testCaseString.split(",") : null;
        if (testCaseArray != null) {
            testCasesToRun = new ArrayList<String>();
            for (String testcase : testCaseArray) {
                testCasesToRun.add(testcase.trim());
            }
        } else {
            testCasesToRun = null;
        }
        return testCasesToRun;
    }

    public static void initializeQuanticGlobalVariables() {
//        System.out.println("Main Branch");
        GemJARGlobalVar.gemJARProperties = PropertyListeners
                .loadProjectProperties(ClassLoader.getSystemResourceAsStream("Quantic.properties"));
        GemJARGlobalVar.projectName = getProjectName();
        ProjectProperties.setProjectProperties(
                ClassLoader.getSystemResourceAsStream(GemJARGlobalVar.projectName + ".properties"));
        GemJARGlobalVar.projectProperty = PropertyListeners.loadProjectProperties(
                ClassLoader.getSystemResourceAsStream(GemJARGlobalVar.projectName + ".properties"));
        GemJARGlobalVar.environment = getProjectEnvironment();
        GemJARGlobalVar.reportName = getProjectReportName();
        GemJARGlobalVar.testCaseFileName = getTestCaseFileName();
        GemJARGlobalVar.testCaseDataJsonPath = System.getProperty("QuanticTestCaseDataJsonPath");
        GemJARGlobalVar.testCasesToRun = getTestCasesToRunFromSystemProperties();
        GemJARGlobalVar.browserInTest = getBrowserToTest();
        String cucumberFlag = GemJARGlobalVar.gemJARProperties.getProperty("cucumber");
        if(cucumberFlag == null || !cucumberFlag.equalsIgnoreCase("y") ){
            if (GemJARGlobalVar.testCaseDataJsonPath != null) {
                TestCaseData.setProjectTestCaseData(GemJARGlobalVar.testCaseDataJsonPath);
            } else {
                TestCaseData
                        .setProjectTestCaseData(ClassLoader.getSystemResourceAsStream(GemJARGlobalVar.testCaseFileName));
            }
        }
        if (GemJARGlobalVar.projectProperty.getProperty("sendMail") == null) {
            GemJARGlobalVar.sendMail = "true";
        } else {
            GemJARGlobalVar.sendMail = GemJARGlobalVar.projectProperty.getProperty("sendMail");
        }
        GemJARGlobalVar.reportLocation = getReportLocation();
        initializeMailingList();
    }

    private static String getReportLocation() {
        try {
            String systemQuanticReportLocation = System.getProperty("QuanticReportLocation");
            String reportLocationFromSystemProperty = ProjectProperties.getProperty("reportLocation");
            String loc = reportLocationFromSystemProperty != null && !reportLocationFromSystemProperty.isEmpty()
                    ? reportLocationFromSystemProperty
                    : (System.getProperty("user.dir") != null ? System.getProperty("user.dir") : "");

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

    public static void initializeMailingList() {
        String mailProperties = GemJARGlobalVar.projectName + "_Mail.properties";
        GemJARGlobalVar.mailingProperty = PropertyListeners.loadProjectProperties(
                ClassLoader.getSystemResourceAsStream(mailProperties));
        GemJARGlobalVar.failMail = mailingProperty.getProperty("failMail");
        GemJARGlobalVar.ccMail = mailingProperty.getProperty("ccMail");
        GemJARGlobalVar.passMail = mailingProperty.getProperty("passMail");
        GemJARGlobalVar.mail = mailingProperty.getProperty("mail");
    }

    public static String getBrowserToTest() {
        String browserName = System.getProperty("QuanticBrowserName");
        String browserNameFromPropertiesFile = GemJARGlobalVar.projectProperty.getProperty("browserName");
        String browser = browserName != null ? browserName
                : browserNameFromPropertiesFile != null ? browserNameFromPropertiesFile : "chrome";
        return browser;
    }

}
