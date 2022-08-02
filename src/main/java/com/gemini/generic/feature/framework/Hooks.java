package com.gemini.generic.feature.framework;

import com.gemini.generic.base.provider.GemJARGlobalVar;
import com.gemini.generic.quartz.reporting.GemTestReporter;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

public class Hooks extends GemJarUtils {

    @BeforeAll
    public static void before_all(){
        loadGemJarConfigData();
        GemTestReporter.startSuite(GemJARGlobalVar.projectName, GemJARGlobalVar.environment);
    }



    @Before
    public void before(Scenario scenario){
        GemTestReporter.startTestCase(scenario.getName(),
                "Feature Framework",
        false);
    }

    @After
    public void after(){
        GemTestReporter.endTestCase();
    }

    @AfterAll
    public static void after_all(){
        GemTestReporter.endSuite(GemJARGlobalVar.reportLocation);
    }
}
