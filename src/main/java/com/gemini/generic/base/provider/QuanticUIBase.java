package com.gemini.generic.base.provider;

import com.gemini.generic.listeners.QuanticTestngTestFilter;
import com.gemini.generic.quartz.reporting.GemTestReporter;

import org.testng.ITestContext;
import org.testng.annotations.*;

import static com.gemini.generic.feature.framework.GemJarUtils.*;

import java.io.IOException;
import java.lang.reflect.Method;

@Listeners(QuanticTestngTestFilter.class)
public class QuanticUIBase extends QuanticGenericUtils {

    @BeforeSuite
    public void beforeSuite(ITestContext iTestContext) {
//        initializeQuanticGlobalVariables();
        GemJARGlobalVar.report_type = "UI Automation";
        loadGemJarConfigData();
        GemTestReporter.startSuite(GemJARGlobalVar.projectName, GemJARGlobalVar.environment);

    }

    @Parameters("browserName")
    @BeforeTest
    public void beforeTest(@Optional String browserName) {
        if (browserName != null) {
            GemJARGlobalVar.browserInTest = browserName;
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
        DriverManager.initializeBrowser(GemJARGlobalVar.browserInTest);
        DriverAction.maximizeBrowser();
        DriverAction.setImplicitTimeOut(Long.parseLong(convertJsonElementToString(getGemJarConfigData("browserTimeOut"))));
        DriverAction.setPageLoadTimeOut(Long.parseLong(convertJsonElementToString(getGemJarConfigData("browserTimeOut"))));
        DriverAction.launchUrl(convertJsonElementToString(getGemJarConfigData("baseURL")));
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

        GemTestReporter.endSuite(GemJARGlobalVar.reportLocation);
        GemEcoUpload.postNewRecord();
//        EmailReport.sendReport();


    }

}