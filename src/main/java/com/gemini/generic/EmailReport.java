package com.gemini.generic;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EmailReport {

    public static void sendReport() {
        if (!GemjarGlobalVar.sendMail.equalsIgnoreCase("false")) {
            String suiteStatus = GemjarGlobalVar.suiteDetail.getAsJsonObject().get("Suits_Details").getAsJsonObject().get("status").getAsString();
            Long suiteStart = GemjarGlobalVar.suiteDetail.getAsJsonObject().get("Suits_Details").getAsJsonObject().get("s_start_time").getAsLong();
            Date date = new Date(suiteStart);
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
            String formattedDate = format.format(date);
            String message = "GEMJAR Email Report ";
            String subject = suiteStatus + " | " + GemjarGlobalVar.environment.toUpperCase() + " | " + GemjarGlobalVar.projectName + " | GEM JAR AUTOMATED SUITE RUN EXECUTED ON " + formattedDate;
            String toMail = GemjarGlobalVar.mail;
            String ccMail = GemjarGlobalVar.ccMail;
            String conditionMail = toMail;
            if (suiteStatus.equals("PASS")) {
                conditionMail = GemjarGlobalVar.passMail;
            } else {
                conditionMail = GemjarGlobalVar.failMail;
            }
            String fromMail = GemjarGlobalVar.fromMail;
            String host = "smtp.gmail.com";
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(GemjarGlobalVar.fromMail, GemjarGlobalVar.fromMailPwd);
                }
            });
//        session.setDebug(true);
            MimeMessage reportMessage = new MimeMessage(session);
            try {
                reportMessage.setFrom(fromMail);
                reportMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
                reportMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccMail));
                reportMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(conditionMail));
                reportMessage.setSubject(subject);
                String path = "";
                if (GemjarGlobalVar.report_type.equals("UI Automation")) {
                    String zipLoc = GemjarGlobalVar.reportLocation + ".zip";
                    zipFolder(Paths.get(GemjarGlobalVar.reportLocation), Paths.get(zipLoc));
                    path = zipLoc;
                } else {
                    path = GemjarGlobalVar.reportLocation + "/" + GemjarGlobalVar.reportName + ".html";
                }
                MimeMultipart multipart = new MimeMultipart();
                MimeBodyPart textContent = new MimeBodyPart();
                MimeBodyPart fileContent = new MimeBodyPart();
                try {
                    textContent.setText(message);
                    File reportLocation = new File(path);
                    fileContent.attachFile(reportLocation);
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

    public static void zipFolder(Path sourceFolderPath, Path zipPath) throws Exception {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
        Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
                Files.copy(file, zos);
                zos.closeEntry();
                return FileVisitResult.CONTINUE;
            }
        });
        zos.close();
    }
}