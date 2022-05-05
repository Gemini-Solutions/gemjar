package com.gemini.quartzReporting;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


public class GemReportingUtility {


    public static void createReport(String suiteDetail, String stepJson) throws IOException {
        String loc = GemTestReporter.ReportLocation;
        String htmlTemplate = IOUtils.toString(ClassLoader.getSystemResourceAsStream("QuanticReport.html"), Charset.defaultCharset());
        htmlTemplate = htmlTemplate.replace("var obj = '';", "var obj = " + suiteDetail + ";");
        LocalDateTime now = LocalDateTime.now();
        //ddmmyyyyhhmmss
        DateTimeFormatter dmyhms = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
        FileUtils.writeStringToFile(new File(loc + "/GemEcoTestReport_" + dmyhms.format(now) + ".html"), htmlTemplate, Charset.defaultCharset());

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
