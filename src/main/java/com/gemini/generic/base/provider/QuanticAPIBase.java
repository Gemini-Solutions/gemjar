package com.gemini.generic.base.provider;

import static com.gemini.generic.feature.framework.GemJarUtils.loadGemJarConfigData;

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

import com.gemini.generic.quartz.reporting.GemTestReporter;
import com.gemini.generic.remote.invocation.ProjectSampleJson;

public class QuanticAPIBase extends QuanticGenericUtils {
    @BeforeSuite
    public void beforeSuite(ITestContext iTestContext) {
//        initializeQuanticGlobalVariables();
        GemJARGlobalVar.report_type = "Api Automation";
        loadGemJarConfigData();
//        String urlFileName = GemJARGlobalVar.projectName + "_" + GemJARGlobalVar.environment + "_Url.properties";
//        InputStream ip = ClassLoader.getSystemResourceAsStream(urlFileName);
//        ProjectApiUrl.initializeApiUrl(ip);
        ProjectSampleJson.loadSampleJson();
        GemTestReporter.startSuite(GemJARGlobalVar.projectName, GemJARGlobalVar.environment);
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
        GemTestReporter.endSuite(GemJARGlobalVar.reportLocation);
        GemEcoUpload.postNewRecord();
//        EmailReport.sendReport();

    }

}