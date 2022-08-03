package com.gemini.generic.quartz.reporting;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.time.Instant;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.gemini.generic.base.provider.GemJARGlobalVar;

public class GemReportingUtility {

    public static void createReport(String suiteDetail, String stepJson, String reportLoc) {
        try {
            String htmlTemplate = IOUtils.toString(ClassLoader.getSystemResourceAsStream("QuanticReport.html"),
                    Charset.defaultCharset());
            htmlTemplate = htmlTemplate.replace("var obj = '';", "var obj = " + suiteDetail + ";");
            GemJARGlobalVar.reportName="GemEcoTestReport_" + Instant.now().toEpochMilli();
            FileUtils.writeStringToFile(new File(reportLoc + "/"+ GemJARGlobalVar.reportName + ".html"), htmlTemplate, Charset.defaultCharset());
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
        return Instant.now().toEpochMilli();
    }

    public static String getMachineName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrentUserName() {
        return System.getProperty("user.name");
    }

}