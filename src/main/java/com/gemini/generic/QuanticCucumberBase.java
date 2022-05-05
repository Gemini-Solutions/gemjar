package com.gemini.generic;

import java.io.File;
import java.net.URISyntaxException;

import org.testng.annotations.BeforeSuite;

import com.gemini.apitest.ProjectApiUrl;
import com.gemini.apitest.ProjectSampleJson;
import com.gemini.listners.PropertyListeners;
import com.gemini.quartzReporting.GemTestReporter;

import io.cucumber.core.gherkin.Step;
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
		QuanticGlobalVar.quanticProperty = PropertyListeners
				.loadProjectProperties(ClassLoader.getSystemResourceAsStream("Quantic.properties"));
		setCucumberProperties();
	}

	private void setCucumberProperties() {

		try {
			String stepDefinitionPackages = QuanticGlobalVar.quanticProperty.getProperty("glueCode");
			System.setProperty("cucumber.glue", "com.gemini.generic," + stepDefinitionPackages);
			System.setProperty("cucumber.features",
					new File(ClassLoader.getSystemResource("features").toURI()).getAbsolutePath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@BeforeAll
	public void beforeAll() {
		QuanticGenericUtils.initializeQuanticGlobalVariables();
		ProjectApiUrl.initializeApiUrl();
		ProjectSampleJson.loadSampleJson();
		GemTestReporter.startSuite(QuanticGlobalVar.projectName, QuanticGlobalVar.environment);
	}

	@Before
	public void before(Scenario scenario) {
		String testcaseName = scenario.getName();
		String featureFileName = new File(scenario.getUri()).getName();
		DriverManager.initializeBrowser(QuanticGlobalVar.browserInTest);
		DriverAction.maximizeBrowser();
		DriverAction.setImplicitTimeOut(Long.parseLong(ProjectProperties.getProperty("browserTimeOut")));
		DriverAction.setPageLoadTimeOut(Long.parseLong(ProjectProperties.getProperty("browserTimeOut")));
		DriverAction.launchUrl(ProjectProperties.getProperty("baseURL"));
		GemTestReporter.startTestCase(testcaseName, featureFileName.substring(0, featureFileName.lastIndexOf('.')),
				false);
	}

	@BeforeStep
	public void beforeStep(Step step, Scenario scenario) {

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
	public void afterAll() {
		GemTestReporter.endSuite();
	}

}
