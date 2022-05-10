package com.gemini.generic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.gemini.listners.PropertyListeners;

public class QuanticGenericUtils extends QuanticGlobalVar {
	public static void setKerberosRequiredConfiguration() {
		try {
			System.setProperty("java.security.krb5.conf",
					new File(ClassLoader.getSystemResource("krb5.conf").toURI()).getAbsolutePath());
			System.setProperty("java.security.auth.login.config",
					new File(ClassLoader.getSystemResource("login.conf").toURI()).getAbsolutePath());
			System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
			System.setProperty("com.sun.net.ssl.checkRevocation", "false");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProjectName() {
		try {
			String sysPropProjectName = System.getProperty("QuanticProjectName");
			String mavenProjectName = QuanticGlobalVar.quanticProperty.getProperty("artifactId");
			String projectName = sysPropProjectName != null ? sysPropProjectName
					: mavenProjectName != null ? mavenProjectName : null;
			return projectName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getProjectEnvironment() {
		String sysPropEnvironment = System.getProperty("QuanticProjectEnvironment");
		String environmentFromPropertiesFile = QuanticGlobalVar.projectProperty.getProperty("environment");
		String environment = sysPropEnvironment != null ? sysPropEnvironment
				: environmentFromPropertiesFile != null ? environmentFromPropertiesFile : "beta";
		return environment;
	}

	public static String getProjectReportName() {
		String sysPropReportName = System.getProperty("QuanticReportName");
		String reportNameFromPropFiles = QuanticGlobalVar.projectProperty.getProperty("reportName");
		String reportName = sysPropReportName != null ? sysPropReportName
				: reportNameFromPropFiles != null ? reportNameFromPropFiles
						: QuanticGlobalVar.projectName + " Test report";
		return reportName;
	}

	public static String getTestCaseFileName() {
		String sysTestCaseFileName = System.getProperty("QuanticTestCaseFileName");
		String testCaseFileNameFromProjProp = QuanticGlobalVar.projectProperty.getProperty("testCaseFileName");
		String testCaseFileName = sysTestCaseFileName != null ? sysTestCaseFileName
				: testCaseFileNameFromProjProp != null ? testCaseFileNameFromProjProp
						: QuanticGlobalVar.projectName + "_testCase.json";
		return testCaseFileName;
	}

	public static List<String> getTestCasesToRunFromSystemProperties() {
		List<String> testCasesToRun;
		String testCaseString = System.getProperty("QuanticTestCasesToRun");
		String[] testCaseArray = testCaseString != null ? testCaseString.split(",") : null;
		if (testCaseArray != null) {
			testCasesToRun = new ArrayList<String>();
			for (String testcase : testCaseArray) {
				testCasesToRun.add(testcase.trim());
			}
		} else {
			testCasesToRun = null;
		}
		return testCasesToRun;
	}

	public static void initializeQuanticGlobalVariables() {
		QuanticGlobalVar.quanticProperty = PropertyListeners
				.loadProjectProperties(ClassLoader.getSystemResourceAsStream("Quantic.properties"));
		QuanticGlobalVar.projectName = getProjectName();
		ProjectProperties.setProjectProperties(
				ClassLoader.getSystemResourceAsStream(QuanticGlobalVar.projectName + ".properties"));
		QuanticGlobalVar.projectProperty = PropertyListeners.loadProjectProperties(
				ClassLoader.getSystemResourceAsStream(QuanticGlobalVar.projectName + ".properties"));
		QuanticGlobalVar.environment = getProjectEnvironment();
		QuanticGlobalVar.reportName = getProjectReportName();
		QuanticGlobalVar.testCaseFileName = getTestCaseFileName();
		QuanticGlobalVar.testCaseDataJsonPath = System.getProperty("QuanticTestCaseDataJsonPath");
		QuanticGlobalVar.testCasesToRun = getTestCasesToRunFromSystemProperties();
		QuanticGlobalVar.browserInTest = getBrowserToTest();
		if (QuanticGlobalVar.testCaseDataJsonPath != null) {
			TestCaseData.setProjectTestCaseData(QuanticGlobalVar.testCaseDataJsonPath);
		} else {
			TestCaseData
					.setProjectTestCaseData(ClassLoader.getSystemResourceAsStream(QuanticGlobalVar.testCaseFileName));
		}
		QuanticGlobalVar.reportLocation = getReportLocation();
	}

	private static String getReportLocation() {
		String systemQuanticReportLocation = System.getProperty("QunaticReportLocation");
		String reportLocationFromSystemProperty = ProjectProperties.getProperty("reportLocation");
		String loc = reportLocationFromSystemProperty != null && !reportLocationFromSystemProperty.isEmpty()
				? reportLocationFromSystemProperty
				: System.getProperty("user.dir");

		return loc;
	}

	public static void initializeMailingList() {
		QuanticGlobalVar.mailingProperty = PropertyListeners.loadProjectProperties(
				ClassLoader.getSystemResourceAsStream(QuanticGlobalVar.projectName + "+_Mail.properties"));
		QuanticGlobalVar.failMail = mailingProperty.getProperty("failMail");
		QuanticGlobalVar.ccMail = mailingProperty.getProperty("ccMail");
		QuanticGlobalVar.passMail = mailingProperty.getProperty("passMail");
		QuanticGlobalVar.mail = mailingProperty.getProperty("mail");
	}

	public static String getBrowserToTest() {
		String browserName = System.getProperty("QuanticBrowserName");
		String browserNameFromPropertiesFile = QuanticGlobalVar.projectProperty.getProperty("browserName");
		String browser = browserName != null ? browserName
				: browserNameFromPropertiesFile != null ? browserNameFromPropertiesFile : "chrome";
		return browser;
	}

}
