package com.gemini.quartzReporting;

import com.gemini.generic.GemjarGlobalVar;
import com.gemini.generic.ProjectProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.time.Instant;

public class GemReportingUtility {

    public static void createReport(String suiteDetail, String stepJson, String reportLoc) {
        try {
            String htmlTemplate = IOUtils.toString(ClassLoader.getSystemResourceAsStream("GemjarReport.html"),
                    Charset.defaultCharset());
            htmlTemplate = htmlTemplate.replace("var obj = '';", "var obj = " + suiteDetail + ";");
            GemjarGlobalVar.reportName="GemEcoTestReport_" + Instant.now().toEpochMilli();
            FileUtils.writeStringToFile(new File(reportLoc + "/"+ GemjarGlobalVar.reportName + ".html"), htmlTemplate, Charset.defaultCharset());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void createReport(String suiteDetail, String stepJson) {
        createReport(suiteDetail, stepJson, null);
    }

    public static long getCurrentTimeInSecond() {
        return Instant.now().getEpochSecond();
    }

    public static long getCurrentTimeInMilliSecond() {
        return Instant.now().getEpochSecond()*1000;
    }

    public static String getMachineName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrentUserName() {
        if (ProjectProperties.containsKey("gemjarUserName"))
        {
            return ProjectProperties.getProperty("gemjarUserName");

        }

        return System.getProperty("user.name");

        //get from properties from same as login user name from jewel
    }

}
