package com.gemini.featureFrameWork;

import com.gemini.generic.GemJARGlobalVar;
import com.gemini.quartzReporting.GemTestReporter;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.cucumber.java.*;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class Hooks extends GemJarUtils {

    @BeforeAll
    public static void before_all(){
        loadGemJarConfigData();
        GemTestReporter.startSuite(GemJARGlobalVar.projectName,GemJARGlobalVar.environment);
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
