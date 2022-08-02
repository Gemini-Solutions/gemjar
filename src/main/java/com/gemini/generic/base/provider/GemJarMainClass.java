package com.gemini.generic.base.provider;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.gemini.generic.quartz.reporting.GemTestReporter;
import com.gemini.generic.remote.invocation.ApiRemoteInvocationImpl;


public class GemJarMainClass extends QuanticGenericUtils {

    @SuppressWarnings({"deprecation", "rawtypes"})
    public static void main(String args[]) {

        String projectName = System.getProperty("projectName");
        if (projectName == null) {
            projectName = "GemJaR";
        }

        String env = System.getProperty("env");
        if (env == null) {
            env = "BETA";
        }

        String reportLocation = System.getProperty("loc");
        if (reportLocation == null) {
            reportLocation = System.getProperty("user.home") + "/GemJaR";

        }

        GemTestReporter.startSuite(projectName, env);

        String path = System.getProperty("path");

        if (!(path == null)) {
            File fr = null;
            try {
                fr = new File(path);

                ApiRemoteInvocationImpl.healthCheck(fr);
                GemTestReporter.endSuite(reportLocation);
                GemEcoUpload.postNewRecord();

                //Sending Email
                String suiteStatus = GemJARGlobalVar.suiteDetail.getAsJsonObject().get("Suits_Details").getAsJsonObject().get("status").getAsString();
                Long suiteStart = GemJARGlobalVar.suiteDetail.getAsJsonObject().get("Suits_Details").getAsJsonObject().get("s_start_time").getAsLong();
                Date date = new Date(suiteStart);
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String formattedDate = format.format(date);
                String message = "GEMJAR Email Report ";
                String subject = suiteStatus + " | " + env + " | " + projectName + " | GEM JAR AUTOMATED SUITE RUN EXECUTED ON " + formattedDate;

                String toMail = System.getProperty("toMail");
                if (!(toMail == null)) {
                    String fromMail = GemJARGlobalVar.fromMail;
                    String host = "smtp.gmail.com";
                    Properties properties = System.getProperties();
                    properties.put("mail.smtp.host", host);
                    properties.put("mail.smtp.port", "465");
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.auth", "true");
                    Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(GemJARGlobalVar.fromMail, GemJARGlobalVar.fromMailPwd);
                        }
                    });
//        session.setDebug(true);
                    MimeMessage reportMessage = new MimeMessage(session);
                    try {
                        reportMessage.setFrom(new InternetAddress(fromMail));
                        reportMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));

                        reportMessage.setSubject(subject);
                        //String path = "";

                        reportLocation = reportLocation + "/" + GemJARGlobalVar.reportName + ".html";

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
            } catch (Exception e) {
                System.out.println("Invalid file path or file does not exists. Enter a valid file path");
                e.printStackTrace();
            }
        } else {
            System.out.println("Path cannot be null. Enter a valid file path");
        }
    }
}




