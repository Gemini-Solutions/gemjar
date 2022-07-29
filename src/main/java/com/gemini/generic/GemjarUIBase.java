package com.gemini.generic;

import com.gemini.listners.GemjarTestngTestFilter;
import com.gemini.quartzReporting.GemTestReporter;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;

@Listeners(GemjarTestngTestFilter.class)
public class GemjarUIBase extends GemjarGenericUtils {

    @BeforeSuite
    public void beforeSuite(ITestContext iTestContext) {
        initializeGemjarGlobalVariables();
        GemjarGlobalVar.report_type = "UI Automation";
        GemTestReporter.startSuite(GemjarGlobalVar.projectName, GemjarGlobalVar.environment);

    }

    @Parameters("browserName")
    @BeforeTest
    public void beforeTest(@Optional String browserName) {
        if (browserName != null) {
            GemjarGlobalVar.browserInTest = browserName;
        }
    }

    @BeforeClass
    public void beforeClass() {

    }

    @BeforeMethod
    public void beforeMethod(Method method) throws IOException {

        String testcaseName = method.getName();
        String testClassName = method.getClass().getSimpleName();
        GemTestReporter.startTestCase(testcaseName, testClassName, false);
        DriverManager.initializeBrowser(GemjarGlobalVar.browserInTest);
        DriverAction.maximizeBrowser();
        DriverAction.setImplicitTimeOut(Long.parseLong(ProjectProperties.getProperty("browserTimeOut")));
        DriverAction.setPageLoadTimeOut(Long.parseLong(ProjectProperties.getProperty("browserTimeOut")));
        DriverAction.launchUrl(ProjectProperties.getProperty("baseURL"));
        TestCaseData.setCurrentTestCaseData(testcaseName);
    }

    @AfterMethod
    public void afterMethod() {
        DriverManager.closeDriver();
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

        GemTestReporter.endSuite(GemjarGlobalVar.reportLocation);
        GemEcoUpload.postNewRecord();
//        EmailReport.sendReport();


    }

}
