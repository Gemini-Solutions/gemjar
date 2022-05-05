package com.gemini.generic;

import com.gemini.generic.*;
import com.gemini.listners.QuanticTestngTestFilter;
import com.gemini.quartzReporting.GemTestReporter;
import com.gemini.quartzReporting.GemTestReporter;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Listeners(QuanticTestngTestFilter.class)
public class QuanticUIBase extends QuanticGenericUtils {


    @BeforeSuite
    public void beforeSuite(ITestContext iTestContext) {
        initializeQuanticGlobalVariables(iTestContext);
        int numberOfTestCasesToRun = iTestContext.getSuite().getAllInvokedMethods().size();
        //Report

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        //hms
        DateTimeFormatter hms = DateTimeFormatter.ofPattern("HHmmss");
        String loc = "";
        try {
            if (System.getProperty("reportLocation") != null && System.getProperty("reportLocation").isEmpty()) {
                loc = System.getProperty("reportLocation");
            } else if (ProjectProperties.getStringPropertyNames().contains("reportLocation")) {
                loc = ProjectProperties.getProperty("reportLocation");
            } else {
                loc = "";
            }
            loc = loc + "Report/" + dtf.format(now) + "/" + hms.format(now);
            QuanticGlobalVar.reportLoc = loc;
        } catch (Exception e) {
            System.out.println("Some Error Occur With reportLocation . Default reportLocation Set");
        }
        QuanticGlobalVar.report_type="UI Automation";
        // Initializing startSuite of Gem-Reporting
        GemTestReporter.startSuite(QuanticGlobalVar.projectName, QuanticGlobalVar.environment, loc);

    }

    @Parameters("browserName")
    @BeforeTest
    public void beforeTest(@Optional String browserName) {
        if (browserName != null) {
            QuanticGlobalVar.browserInTest = browserName;
        }
    }

    @BeforeClass
    public void beforeClass() {

    }

    @BeforeMethod
    public void beforeMethod(Method method) throws IOException {

        String testcaseName = method.getName();
        String productType = ProjectProperties.getProperty("productType") == null ? "GemJavaProject" : ProjectProperties.getProperty("productType");
        GemTestReporter.startTestCase(testcaseName, "test", productType, false);
        DriverManager.initializeBrowser(QuanticGlobalVar.browserInTest);
        DriverAction.maximizeBrowser();
        DriverAction.setImplicitTimeOut(Long.parseLong(ProjectProperties.getProperty("browserTimeOut")));
        DriverAction.setPageLoadTimeOut(Long.parseLong(ProjectProperties.getProperty("browserTimeOut")));
        DriverAction.launchUrl(ProjectProperties.getProperty("baseURL"));
        TestCaseData.setCurrentTestCaseData(testcaseName);
        //Report

    }

    @AfterMethod
    public void afterMethod() {
        DriverManager.closeDriver();
        //Report
//        GemTestReporter.endTestCase();
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
        //Report
//        GemTestReporter.endSuite();
        GemTestReporter.endSuite();
        //send mail
//        QuanticGenericUtils.sendMail();
    }


}
