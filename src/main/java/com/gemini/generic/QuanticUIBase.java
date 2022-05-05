package com.gemini.generic;

import java.io.IOException;
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
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.gemini.listners.QuanticTestngTestFilter;
import com.gemini.quartzReporting.GemTestReporter;

@Listeners(QuanticTestngTestFilter.class)
public class QuanticUIBase extends QuanticGenericUtils {

	@BeforeSuite
	public void beforeSuite(ITestContext iTestContext) {
		initializeQuanticGlobalVariables();
		GemTestReporter.startSuite(QuanticGlobalVar.projectName, QuanticGlobalVar.environment);

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
		String testClassName = method.getClass().getSimpleName();
		GemTestReporter.startTestCase(testcaseName, testClassName, false);
		DriverManager.initializeBrowser(QuanticGlobalVar.browserInTest);
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
		GemTestReporter.endSuite(QuanticGlobalVar.reportLocation);
	}

}
