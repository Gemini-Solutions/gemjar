package com.gemini.automation.ApiTest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.gemini.automation.generic.QuanticGlobalVar;

public class ProjectApiUrl {
    private static Properties properties;

    public static void initializeApiUrl(){
        try{
            properties = new Properties();
            String urlFileName = QuanticGlobalVar.projectName+"_"+ QuanticGlobalVar.environment+"_Url.properties";
            properties.load(ClassLoader.getSystemResourceAsStream(urlFileName));

        } catch (IOException e){
             // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void initializeApiUrl(InputStream ip){
        try{
            properties = new Properties();
            //properties.load(ClassLoader.getSystemResourceAsStream(urlFileName));
            properties.load(ip);
        } catch (IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getUrl(String urlName){
        String urlValue = properties.getProperty(urlName);
        return urlValue;
    }

    public static void updateUrl(String urlName, String urlValue){
        properties.setProperty(urlName,urlValue);
    }


}
