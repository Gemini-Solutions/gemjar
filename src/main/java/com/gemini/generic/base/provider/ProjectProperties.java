package com.gemini.generic.base.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import com.gemini.generic.feature.framework.GemJarUtils;

public class ProjectProperties {
	private static Properties projectProperties;

	public static void setProjectProperties(InputStream inputStream) {
		try {
			projectProperties = new Properties();
			projectProperties.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setProjectProperties(String pathname) {
		try {
			projectProperties = new Properties();
			File file = new File(pathname);

			FileInputStream fileInputStream = new FileInputStream(file);
			projectProperties.load(fileInputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		try {
			String value = GemJarUtils.getGemJarConfigData(key).getAsString();
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void setProperty(String key, String value) {
		try {
			projectProperties.setProperty(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Boolean containsKey(String key) {
		try {
			Boolean keyStatus = projectProperties.containsKey(key);
			return keyStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Boolean isEmpty() {
		try {
			Boolean keyStatus = projectProperties.isEmpty();
			return keyStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Set<Object> getKeySet() {
		try {
			Set<Object> keyStatus = projectProperties.keySet();
			return keyStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Set<String> getStringPropertyNames() {
		try {
			Set<String> keyStatus = projectProperties.stringPropertyNames();
			return keyStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getSize() {
		try {
			int keyStatus = projectProperties.size();
			return keyStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
