package com.gemini.generic;

import com.gemini.listners.PropertyListeners;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GemjarGenericUtils extends GemjarGlobalVar {
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
            String sysPropProjectName = System.getProperty("GemjarProjectName");
            String mavenProjectName = GemjarGlobalVar.GemjarProperty.getProperty("artifactId");
            String projectName = sysPropProjectName != null ? sysPropProjectName
                    : mavenProjectName != null ? mavenProjectName : null;
            return projectName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getProjectEnvironment() {
        String sysPropEnvironment = System.getProperty("GemjarProjectEnvironment");
        String environmentFromPropertiesFile = GemjarGlobalVar.projectProperty.getProperty("environment");
        String environment = sysPropEnvironment != null ? sysPropEnvironment
                : environmentFromPropertiesFile != null ? environmentFromPropertiesFile : "beta";
        return environment;
    }

    public static String getProjectReportName() {
        String sysPropReportName = System.getProperty("GemjarReportName");
        String reportNameFromPropFiles = GemjarGlobalVar.projectProperty.getProperty("reportName");
        String reportName = sysPropReportName != null ? sysPropReportName
                : reportNameFromPropFiles != null ? reportNameFromPropFiles
                : GemjarGlobalVar.projectName + " Test report";
        return reportName;
    }

    public static String getTestCaseFileName() {
        String sysTestCaseFileName = System.getProperty("GemjarTestCaseFileName");
        String testCaseFileNameFromProjProp = GemjarGlobalVar.projectProperty.getProperty("testCaseFileName");
        String testCaseFileName = sysTestCaseFileName != null ? sysTestCaseFileName
                : testCaseFileNameFromProjProp != null ? testCaseFileNameFromProjProp
                : GemjarGlobalVar.projectName + "_testCase.json";
        return testCaseFileName;
    }

    public static List<String> getTestCasesToRunFromSystemProperties() {
        List<String> testCasesToRun;
        String testCaseString = System.getProperty("GemjarTestCasesToRun");
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

    public static void initializeGemjarGlobalVariables() {
        GemjarGlobalVar.GemjarProperty = PropertyListeners
                .loadProjectProperties(ClassLoader.getSystemResourceAsStream("Gemjar.properties"));
        GemjarGlobalVar.projectName = getProjectName();
        ProjectProperties.setProjectProperties(
                ClassLoader.getSystemResourceAsStream(GemjarGlobalVar.projectName + ".properties"));
        GemjarGlobalVar.projectProperty = PropertyListeners.loadProjectProperties(
                ClassLoader.getSystemResourceAsStream(GemjarGlobalVar.projectName + ".properties"));
        GemjarGlobalVar.environment = getProjectEnvironment();
        GemjarGlobalVar.reportName = getProjectReportName();
        GemjarGlobalVar.testCaseFileName = getTestCaseFileName();
        GemjarGlobalVar.testCaseDataJsonPath = System.getProperty("GemjarTestCaseDataJsonPath");
        GemjarGlobalVar.testCasesToRun = getTestCasesToRunFromSystemProperties();
        GemjarGlobalVar.browserInTest = getBrowserToTest();
        if (GemjarGlobalVar.testCaseDataJsonPath != null) {
            TestCaseData.setProjectTestCaseData(GemjarGlobalVar.testCaseDataJsonPath);
        } else {
            TestCaseData
                    .setProjectTestCaseData(ClassLoader.getSystemResourceAsStream(GemjarGlobalVar.testCaseFileName));
        }
        if (GemjarGlobalVar.projectProperty.getProperty("sendMail") == null) {
            GemjarGlobalVar.sendMail = "true";
        } else {
            GemjarGlobalVar.sendMail = GemjarGlobalVar.projectProperty.getProperty("sendMail");

        }
        GemjarGlobalVar.reportLocation = getReportLocation();
        initializeMailingList();
    }

    private static String getReportLocation() {
        try {
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
        String mailProperties = GemjarGlobalVar.projectName + "_Mail.properties";
        GemjarGlobalVar.mailingProperty = PropertyListeners.loadProjectProperties(
                ClassLoader.getSystemResourceAsStream(mailProperties));
        GemjarGlobalVar.failMail = mailingProperty.getProperty("failMail");
        GemjarGlobalVar.ccMail = mailingProperty.getProperty("ccMail");
        GemjarGlobalVar.passMail = mailingProperty.getProperty("passMail");
        GemjarGlobalVar.mail = mailingProperty.getProperty("mail");
    }

    public static String getBrowserToTest() {
        String browserName = System.getProperty("GemjarBrowserName");
        String browserNameFromPropertiesFile = GemjarGlobalVar.projectProperty.getProperty("browserName");
        String browser = browserName != null ? browserName
                : browserNameFromPropertiesFile != null ? browserNameFromPropertiesFile : "chrome";
        return browser;
    }

}
