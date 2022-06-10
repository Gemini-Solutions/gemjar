package com.gemini.generic;

import com.gemini.apitest.ProjectApiUrl;
import com.gemini.apitest.ProjectSampleJson;
import com.gemini.quartzReporting.GemTestReporter;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.InputStream;
import java.lang.reflect.Method;

public class QuanticAPIBase extends QuanticGenericUtils {
    @BeforeSuite
    public void beforeSuite(ITestContext iTestContext) {
        initializeQuanticGlobalVariables();
        QuanticGlobalVar.report_type = "Api Automation";
        String urlFileName = QuanticGlobalVar.projectName + "_" + QuanticGlobalVar.environment + "_Url.properties";
        InputStream ip = ClassLoader.getSystemResourceAsStream(urlFileName);
        ProjectApiUrl.initializeApiUrl(ip);
        ProjectSampleJson.loadSampleJson();
        GemTestReporter.startSuite(QuanticGlobalVar.projectName, QuanticGlobalVar.environment);
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
        String testClassName = method.getClass().getSimpleName();
        TestCaseData.setCurrentTestCaseData(testcaseName);
        GemTestReporter.startTestCase(testcaseName, testClassName, false);
    }

    @AfterMethod
    public void afterMethod() {
        // Report
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
        GemTestReporter.endSuite(QuanticGlobalVar.reportLocation);
        GemEcoUpload.postNewRecord();
//        EmailReport.sendReport();

    }

}