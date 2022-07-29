package com.gemini.generic;

import com.gemini.apitest.ProjectApiUrl;
import com.gemini.apitest.ProjectSampleJson;
import com.gemini.listners.PropertyListeners;
import com.gemini.quartzReporting.GemTestReporter;
import io.cucumber.core.gherkin.Step;
import io.cucumber.java.*;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.net.URISyntaxException;

public class GemjarCucumberBase extends AbstractTestNGCucumberTests {

    @BeforeSuite
    public void beforeSuite() {
        GemjarGlobalVar.GemjarProperty = PropertyListeners
                .loadProjectProperties(ClassLoader.getSystemResourceAsStream("Gemjar.properties"));
        setCucumberProperties();
    }

    private void setCucumberProperties() {

        try {
            String stepDefinitionPackages = GemjarGlobalVar.GemjarProperty.getProperty("glueCode");
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
        GemjarGenericUtils.initializeGemjarGlobalVariables();
        ProjectApiUrl.initializeApiUrl();
        ProjectSampleJson.loadSampleJson();
        GemTestReporter.startSuite(GemjarGlobalVar.projectName, GemjarGlobalVar.environment);
    }

    @Before
    public void before(Scenario scenario) {
        String testcaseName = scenario.getName();
        String featureFileName = new File(scenario.getUri()).getName();
        DriverManager.initializeBrowser(GemjarGlobalVar.browserInTest);
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
        GemTestReporter.endSuite(GemjarGlobalVar.reportLocation);
    }

}
