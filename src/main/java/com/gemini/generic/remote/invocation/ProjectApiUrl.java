package com.gemini.generic.remote.invocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.gemini.generic.base.provider.GemJARGlobalVar;
import com.gemini.generic.feature.framework.GemJarUtils;

public class ProjectApiUrl {
    private static Properties properties;

    public static void initializeApiUrl() {
        try {
            properties = new Properties();
            String urlFileName = GemJARGlobalVar.projectName + "_" + GemJARGlobalVar.environment + "_Url.properties";
            properties.load(ClassLoader.getSystemResourceAsStream(urlFileName));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void initializeApiUrl(InputStream ip) {
        try {
            properties = new Properties();
            properties.load(ip);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getUrl(String urlName) {
        String urlValue = GemJarUtils.getGemJarConfigData(urlName).getAsString();
        return urlValue;
    }

    public static void updateUrl(String urlName, String urlValue) {
        properties.setProperty(urlName, urlValue);
    }

}
