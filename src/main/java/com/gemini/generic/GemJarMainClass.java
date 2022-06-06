package com.gemini.generic;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gemini.apitest.ApiClientConnect;
import com.gemini.quartzReporting.GemTestReporter;
import com.google.gson.JsonArray;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class GemJarMainClass extends QuanticGenericUtils{

    @SuppressWarnings({ "deprecation", "rawtypes" })
    public static void main(String args[]) {

        String projectName = System.getProperty("projectName");
        if(projectName ==null){
            projectName = "GemJaR";
        }

        String env = System.getProperty("env");
        if(env ==null){
            env = "BETA";
        }

        GemTestReporter.startSuite(projectName,env);

        String path = System.getProperty("path");

        if(path==null){
            path="/home/harshit/Desktop/Xls/jsonFile";
        }

        String reportLocation = System.getProperty("loc");

        if(reportLocation==null){
            reportLocation=System.getProperty("user.home")+"/GemJaR";

        }

        File fr = new File(path);
        JsonArray arr = ApiClientConnect.healthCheck(fr);
        //System.out.println(arr);


        GemTestReporter.endSuite(reportLocation);
        GemEcoUpload.postNewRecord();

        //Sending Email
        String suiteStatus = QuanticGlobalVar.suiteDetail.getAsJsonObject().get("Suits_Details").getAsJsonObject().get("status").getAsString();
        Long suiteStart = QuanticGlobalVar.suiteDetail.getAsJsonObject().get("Suits_Details").getAsJsonObject().get("s_start_time").getAsLong();
        Date date = new Date(suiteStart);
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = format.format(date);
        String message = "GEMJAR Email Report ";
        String subject = suiteStatus + " | " + env + " | " + projectName + " | GEM JAR AUTOMATED SUITE RUN EXECUTED ON " + formattedDate;

        String toMail = System.getProperty("toMail");
        if(!(toMail==null)){
            String fromMail = "helloraghavhere1111@gmail.com";
            String host = "smtp.gmail.com";
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("helloraghavhere1111@gmail.com", "fdbeedwjkywcjdzc");
                }
            });
//        session.setDebug(true);
            MimeMessage reportMessage = new MimeMessage(session);
            try {
                reportMessage.setFrom(fromMail);
                reportMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));

                reportMessage.setSubject(subject);
                //String path = "";

                reportLocation = reportLocation + "/" + QuanticGlobalVar.reportName + ".html";

                MimeMultipart multipart = new MimeMultipart();
                MimeBodyPart textContent = new MimeBodyPart();
                MimeBodyPart fileContent = new MimeBodyPart();
                try {
                    textContent.setText(message);
                    File reportLocationFile = new File(reportLocation);
                    fileContent.attachFile(reportLocationFile);
                    multipart.addBodyPart(textContent);
                    multipart.addBodyPart(fileContent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                reportMessage.setContent(multipart);
                Transport.send(reportMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}



