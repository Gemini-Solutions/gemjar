package com.gemini.automation.generic;

import com.gemini.automation.listners.PropertyListeners;
import com.gemini.automation.listners.QuanticTestngTestFilter;
import org.testng.IMethodInterceptor;
import org.testng.TestNG;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class QuanticMainClass {

    @SuppressWarnings({"deprecation", "rawtypes"})
    public static void main(String args[]) {
        Properties quanticProperty = PropertyListeners.loadProjectProperties(ClassLoader.getSystemResourceAsStream("Quantic.properties"));
        String testPackageName = quanticProperty.getProperty("testPackageName");
        String projectName = quanticProperty.getProperty("artifactId");
        TestNG testNG = new TestNG();
        testNG.setDefaultSuiteName(projectName);
        testNG.setDefaultTestName(projectName);
        testNG.setUseDefaultListeners(false);
        IMethodInterceptor quanticTestFilter = new QuanticTestngTestFilter();
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
        testNG.setMethodInterceptor(quanticTestFilter);
        testNG.setTestClasses(testClassArray.toArray(new Class[testClassArray.size()]));
        testNG.run();
    }
}
