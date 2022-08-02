package com.gemini.generic.base.provider;

import java.io.File;
import java.net.URISyntaxException;

import org.testng.annotations.BeforeSuite;

import com.gemini.generic.listners.PropertyListeners;
import com.gemini.generic.quartz.reporting.GemTestReporter;
import com.gemini.generic.remote.invocation.ProjectApiUrl;
import com.gemini.generic.remote.invocation.ProjectSampleJson;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import io.cucumber.testng.AbstractTestNGCucumberTests;



public class QuanticCucumberBase extends AbstractTestNGCucumberTests {

	@BeforeSuite
	public void beforeSuite() {
		GemJARGlobalVar.gemJARProperties = PropertyListeners
				.loadProjectProperties(ClassLoader.getSystemResourceAsStream("Quantic.properties"));
		setCucumberProperties();
	}

	private void setCucumberProperties() {

		try {
			String stepDefinitionPackages = GemJARGlobalVar.gemJARProperties.getProperty("glueCode");
			System.setProperty("cucumber.glue", "com.gemini.generic.base.provider," + stepDefinitionPackages);
			System.setProperty("cucumber.features",
					new File(ClassLoader.getSystemResource("features").toURI()).getAbsolutePath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@BeforeAll
	public static void before_all() {
		QuanticGenericUtils.initializeQuanticGlobalVariables();
		ProjectApiUrl.initializeApiUrl();
		ProjectSampleJson.loadSampleJson();
		GemTestReporter.startSuite(GemJARGlobalVar.projectName, GemJARGlobalVar.environment);
	}

	@Before
	public void before(Scenario scenario) {
		String testcaseName = scenario.getName();
		String featureFileName = new File(scenario.getUri()).getName();
		DriverManager.initializeBrowser(GemJARGlobalVar.browserInTest);
		DriverAction.maximizeBrowser();
		DriverAction.setImplicitTimeOut(Long.parseLong(ProjectProperties.getProperty("browserTimeOut")));
		DriverAction.setPageLoadTimeOut(Long.parseLong(ProjectProperties.getProperty("browserTimeOut")));
		DriverAction.launchUrl(ProjectProperties.getProperty("baseURL"));
		GemTestReporter.startTestCase(testcaseName, featureFileName.substring(0, featureFileName.lastIndexOf('.')),
				false);
	}


	@BeforeStep
	public void before_step(Scenario scenario){
		System.out.println(scenario.getId());
	}

	@AfterStep
	public void afterStep() {

	}

	@After
	public void after(Scenario scenario) {
		DriverManager.closeDriver();
		GemTestReporter.endTestCase();
	}

	@AfterAll
	public static void after_all()  {
		GemTestReporter.endSuite(GemJARGlobalVar.reportLocation);
	}

}
