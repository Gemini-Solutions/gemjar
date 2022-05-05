package com.gemini.generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestCaseData {

	private static JsonObject projectTestCaseData = new JsonObject();
	private static ThreadLocal<JsonObject> testCaseData = new ThreadLocal<JsonObject>();
	private static ThreadLocal<String> testCaseCategory = new ThreadLocal<String>();// optional
	private static ThreadLocal<String> testCaseScenarioID = new ThreadLocal<String>();// optional
	private static ThreadLocal<String> testCaseRunFlag = new ThreadLocal<String>();// optional
	private static ThreadLocal<String> testCaseNameThread = new ThreadLocal<String>();
	private static ThreadLocal<JsonObject> testCaseInputData = new ThreadLocal<JsonObject>();

	// function for get key:value in testCaseData outside inputData

	public static void setProjectTestCaseData(InputStream input) {
		try {
			JsonElement testCaseJsonElement = JsonParser.parseString(IOUtils.toString(input, Charset.defaultCharset()));
			projectTestCaseData = testCaseJsonElement.getAsJsonObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setProjectTestCaseData(String pathname) {
		try {
			File testCaseDataFile = new File(pathname);
			FileInputStream fileInputStream = new FileInputStream(testCaseDataFile);
			JsonElement testCaseJsonElement = JsonParser
					.parseString(IOUtils.toString(fileInputStream, Charset.defaultCharset()));
			projectTestCaseData = testCaseJsonElement.getAsJsonObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setCurrentTestCaseData(String testCaseName) {
		try {
			JsonObject testData = projectTestCaseData.get(testCaseName).getAsJsonObject();
			testCaseData.set(testData);
			testCaseNameThread.set(testCaseName);
			testCaseCategory.set(testCaseData.get().get("category").getAsJsonPrimitive().getAsString());
			testCaseScenarioID.set(testCaseData.get().get("scenarioID").getAsJsonPrimitive().getAsString());
			testCaseRunFlag.set(testCaseData.get().get("runFlag").getAsJsonPrimitive().getAsString());
			testCaseInputData.set(testCaseData.get().get("inputData").getAsJsonObject());
		} catch (Exception e) {
			e.printStackTrace();
			testCaseData.set(null);
		}
	}

	public static String getCurrentTestCaseName() {
		try {
			return testCaseNameThread.get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JsonObject getCurrentTestCaseData() {
		try {
			return testCaseData.get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getTestCaseCategory() {
		return testCaseCategory.get();
	}

	public static String getTestCaseID() {
		return testCaseScenarioID.get();
	}

	public static JsonObject getTestCaseInputData() {
		return testCaseInputData.get();
	}

	public static String getTestCaseRunFlag() {
		return testCaseRunFlag.get();
	}

	public static List<String> getTypeOfTestCases(String type) {
		return null;
	}
}
