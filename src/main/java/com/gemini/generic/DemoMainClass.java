package com.gemini.generic;

import com.gemini.listners.PropertyListeners;
import com.gemini.listners.GemjarTestngTestFilter;
import org.reflections.Reflections;
import org.testng.IMethodInterceptor;
import org.testng.TestNG;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class DemoMainClass {

    @SuppressWarnings({"deprecation", "rawtypes"})
    public static void main(String args[]) {
        Properties gemjarProperty = PropertyListeners
                .loadProjectProperties(ClassLoader.getSystemResourceAsStream("Gemjar.properties"));
        String testPackageName = gemjarProperty.getProperty("testPackageName");
        String projectName = gemjarProperty.getProperty("artifactId");
        TestNG testNG = new TestNG();
        testNG.setDefaultSuiteName(projectName);
        testNG.setDefaultTestName(projectName);
        testNG.setUseDefaultListeners(false);
        IMethodInterceptor gemjarTestFilter = new GemjarTestngTestFilter();
        Reflections reflections = new Reflections(new String(testPackageName));
        Set<String> testClasses = reflections.getAllTypes();
        List<Class> testClassArray = new ArrayList<Class>();
        for (String testClass : testClasses) {
            if (testClass.contains(testPackageName)) {
                try {
                    testClassArray.add(Class.forName(testClass));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        testNG.setMethodInterceptor(gemjarTestFilter);
        testNG.setTestClasses(testClassArray.toArray(new Class[testClassArray.size()]));
        testNG.run();
    }
}
