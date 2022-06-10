package com.gemini.generic;

import org.apache.http.client.utils.URIBuilder;

import java.util.Map;

public class ParameterizedUrl {
	public static String getParameterizedUrl(String url, Map<String, String> params) {

		try {
			URIBuilder ub = new URIBuilder(url);
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				ub.addParameter(key, value);
			}
			return ub.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}