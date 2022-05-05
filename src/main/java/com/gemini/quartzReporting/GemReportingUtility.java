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
	
	
	public static void createReport(String suiteDetail, String stepJson,String  reportLoc) {
		try {String htmlTemplate = IOUtils.toString(ClassLoader.getSystemResourceAsStream("QuanticReport.html"), Charset.defaultCharset());
		htmlTemplate = htmlTemplate.replace("var obj = '';","var obj = "+suiteDetail+";");
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
			
		DateTimeFormatter dmyhms = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");

		if(reportLoc == null) {
			
				FileUtils.writeStringToFile(new File("Report/"+dtf.format(now)+"/"+"GemEcoTestReport_"+dmyhms.format(now)+".html"), htmlTemplate, Charset.defaultCharset());	
		}
		else{
			FileUtils.writeStringToFile(new File(reportLoc+"/GemEcoTestReport.html"), htmlTemplate, Charset.defaultCharset());
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	
	public static void createReport(String suiteDetail, String stepJson) {
		createReport(suiteDetail, stepJson,null);
	}
	
	public static long getCurrentTimeInSecond() {
		return Instant.now().getEpochSecond();
	}
	
	public static long getCurrentTimeInMilliSecond() {
		return Instant.now().toEpochMilli();
	}
	
	public static  String getMachineName() {
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
