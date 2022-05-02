package com.gemini.automation.generic;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.testng.ITestContext;

import com.gemini.automation.listners.PropertyListeners;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.*;
import javax.activation.*;


public class QuanticGenericUtils extends QuanticGlobalVar {
    public void setKerberosRequiredConfiguration() {
        try {
            System.setProperty("java.security.krb5.conf", new File(getClass().getClassLoader().getResource("krb5.conf").toURI()).getAbsolutePath());
            System.setProperty("java.security.auth.login.config", new File(getClass().getClassLoader().getResource("login.conf").toURI()).getAbsolutePath());
            System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
            System.setProperty("com.sun.net.ssl.checkRevocation", "false");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProjectName(ITestContext iTestContext) {
        try {
            String sysPropProjectName = System.getProperty("QuanticProjectName");
            String testngProjectName = iTestContext.getSuite().getParameter("QuanticProjectName");
            String mavenProjectName = QuanticGlobalVar.quanticProperty.getProperty("artifactId");
            String projectName = sysPropProjectName != null ? testngProjectName : mavenProjectName != null ? mavenProjectName : null;
            return projectName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getProjectEnvironment() {
        String sysPropEnvironment = System.getProperty("QuanticProjectEnvironment");
        String environmentFromPropertiesFile = QuanticGlobalVar.projectProperty.getProperty("environment");
        String environment = sysPropEnvironment != null ? sysPropEnvironment : environmentFromPropertiesFile != null ? environmentFromPropertiesFile : "beta";
        return environment;
    }

    public String getProjectReportName() {
        String sysPropReportName = System.getProperty("QuanticReportName");
        String reportNameFromPropFiles = QuanticGlobalVar.projectProperty.getProperty("reportName");
        String reportName = sysPropReportName != null ? sysPropReportName : reportNameFromPropFiles != null ? reportNameFromPropFiles : QuanticGlobalVar.projectName + " Test report";
        return reportName;
    }

    public String getTestCaseFileName() {
        String sysTestCaseFileName = System.getProperty("QuanticTestCaseFileName");
        String testCaseFileNameFromProjProp = QuanticGlobalVar.projectProperty.getProperty("testCaseFileName");
        String testCaseFileName = sysTestCaseFileName != null ? sysTestCaseFileName : testCaseFileNameFromProjProp != null ? testCaseFileNameFromProjProp : QuanticGlobalVar.projectName + "_testCase.json";
        return testCaseFileName;
    }

    public List<String> getTestCasesToRunFromSystemProperties() {
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

    public void initializeQuanticGlobalVariables(ITestContext iTestContext) {
        QuanticGlobalVar.quanticProperty = PropertyListeners.loadProjectProperties(ClassLoader.getSystemResourceAsStream("Quantic.properties"));
        QuanticGlobalVar.projectName = getProjectName(iTestContext);
//        System.out.println("properties file = "+QuanticGlobalVar.projectName + ".properties");
        ProjectProperties.setProjectProperties(ClassLoader.getSystemResourceAsStream(QuanticGlobalVar.projectName + ".properties"));
        QuanticGlobalVar.projectProperty = PropertyListeners.loadProjectProperties(ClassLoader.getSystemResourceAsStream(QuanticGlobalVar.projectName + ".properties"));
        QuanticGlobalVar.environment = getProjectEnvironment();
        QuanticGlobalVar.reportName = getProjectReportName();
        QuanticGlobalVar.testCaseFileName = getTestCaseFileName();
        QuanticGlobalVar.testCaseDataJsonPath = System.getProperty("QuanticTestCaseDataJsonPath");
        QuanticGlobalVar.testCasesToRun = getTestCasesToRunFromSystemProperties();
        QuanticGlobalVar.browserInTest = getBrowserToTest();
        if (QuanticGlobalVar.testCaseDataJsonPath != null) {
            TestCaseData.setProjectTestCaseData(QuanticGlobalVar.testCaseDataJsonPath);
        } else {
            TestCaseData.setProjectTestCaseData(ClassLoader.getSystemResourceAsStream(QuanticGlobalVar.testCaseFileName));
        }
        //error
//        initializeMailingList();
    }

    public void initializeMailingList() {
        QuanticGlobalVar.mailingProperty = PropertyListeners.loadProjectProperties(ClassLoader.getSystemResourceAsStream(QuanticGlobalVar.projectName + "+_Mail.properties"));
        QuanticGlobalVar.failMail = mailingProperty.getProperty("failMail");
        QuanticGlobalVar.ccMail = mailingProperty.getProperty("ccMail");
        QuanticGlobalVar.passMail = mailingProperty.getProperty("passMail");
        QuanticGlobalVar.mail = mailingProperty.getProperty("mail");
    }

    public String getBrowserToTest() {
        String browserName = System.getProperty("QuanticBrowserName");
        String browserNameFromPropertiesFile = QuanticGlobalVar.projectProperty.getProperty("browserName");
        String browser = browserName != null ? browserName : browserNameFromPropertiesFile != null ? browserNameFromPropertiesFile : "chrome";
        return browser;
    }

}
