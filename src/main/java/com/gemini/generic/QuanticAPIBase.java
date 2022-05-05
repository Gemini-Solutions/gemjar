package com.gemini.generic;

import java.io.InputStream;
import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.gemini.apitest.ProjectApiUrl;
import com.gemini.apitest.ProjectSampleJson;
import com.gemini.quartzReporting.GemTestReporter;

public class QuanticAPIBase extends QuanticGenericUtils {
    @BeforeSuite
    public void beforeSuite(ITestContext iTestContext) {
//        setKerberosRequiredConfiguration();
        initializeQuanticGlobalVariables(iTestContext);
       // int numberOfTestCasesToRun = iTestContext.getSuite().getAllInvokedMethods().size();
        //Report initialize
        String urlFileName = QuanticGlobalVar.projectName + "_" + QuanticGlobalVar.environment + "_Url.properties";
        InputStream ip = ClassLoader.getSystemResourceAsStream(urlFileName);
        ProjectApiUrl.initializeApiUrl(ip);
        ProjectSampleJson.loadSampleJson();


        String loc = null;
        try {
            if (quanticProperty.containsKey("reportLocation")) {
                loc = quanticProperty.getProperty("reportLocation");
            } else if (ProjectProperties.getStringPropertyNames().contains("reportLocation")) {
                loc = ProjectProperties.getProperty("reportLocation");
            } else {
                loc = null;
            }
        } catch (Exception e) {
            System.out.println("Some Error Occur With reportLocation . Default reportLocation Set");
        }


        // Initializing startSuite of Gem-Reporting
        GemTestReporter.startSuite(QuanticGlobalVar.projectName, QuanticGlobalVar.environment, loc);
    }

    @BeforeTest
    public void beforeTest() {
    }

    @BeforeClass
    public void beforeClass() {
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        String testcaseName = method.getName();
        TestCaseData.setCurrentTestCaseData(testcaseName);


        String productType = ProjectProperties.getProperty("productType") == null ? "GemJavaProject" : ProjectProperties.getProperty("productType");
        GemTestReporter.startTestCase(testcaseName, "test", productType, false);
        GemTestReporter.startTestCase(testcaseName, "test", "GemJavaProject", false);

    }

    @AfterMethod
    public void afterMethod() {
        //Report
        GemTestReporter.endTestCase();
    }

    @AfterClass
    public void afterClass() {

    }

    @AfterTest
    public void afterTest() {
    }

    @AfterSuite
    public void afterSuite() {
        //Report execution
        GemTestReporter.endSuite();
        //Report mail
    }
}