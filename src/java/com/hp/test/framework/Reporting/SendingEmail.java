package com.hp.test.framework.Reporting;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendingEmail {
static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SendingEmail.class.getName());
    public static void sendmail(ArrayList<String> To_List, String Jenkins_location, String Attachment_file_path, String Screenshot_path) {

        
        Properties properties = System.getProperties();
        ReportingProperties reportingproperties=new ReportingProperties();
        //String mailHost=reportingproperties.getProperty("MAILHOST");
        String SmtpAddress=reportingproperties.getProperty("SMTPADDRESS");
        String FromAddress= reportingproperties.getProperty("FROMADDRESS"); 
        String Subject=reportingproperties.getProperty("SUBJECT");
        String from = FromAddress;//change accordingly
        reportingproperties=null;
        properties.setProperty("mail.smtp.host", SmtpAddress);

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            for (int i = 0; i < To_List.size(); i++) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(To_List.get(i)));
            }
        
            message.setSubject(Subject);

         
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            String filename = Attachment_file_path;
            DataSource source = new FileDataSource(filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(filename.substring(filename.lastIndexOf("\\") + 1, filename.length()));

            Multipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "Click this link for detailed report <a href=" + Jenkins_location + ">    <b>" + Jenkins_location + "</b></a><br><br><br><img src=\"cid:image\">";
            messageBodyPart.setContent(htmlText, "text/html");
            // add it
            multipart.addBodyPart(messageBodyPart);

            // second part (the image)
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource(
                    Screenshot_path);

            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");

            // add image to the multipart
            multipart.addBodyPart(messageBodyPart);

    //5) create Multipart object and add MimeBodyPart objects to this object    
            multipart.addBodyPart(messageBodyPart2);

            //6) set the multiplart object to the message object
            message.setContent(multipart);

            //7) send message
            Transport.send(message);

            System.out.println("Sending build report is completed");
        } catch (MessagingException ex) {
            System.out.println("Exception in sending mail" + ex.getMessage());
        }
    }
    
    
    public static void sendmail(ArrayList<String> To_List, String Jenkins_location, List<String> list_of_screenShots,List<String> html_fileList) {

        
        Properties properties = System.getProperties();
        ReportingProperties reportingproperties=new ReportingProperties();
        //String mailHost=reportingproperties.getProperty("MAILHOST");
        String SmtpAddress=reportingproperties.getProperty("SMTPADDRESS");
        String FromAddress= reportingproperties.getProperty("FROMADDRESS"); 
        String Subject=reportingproperties.getProperty("SUBJECT");
        String ScreenShotsDir=reportingproperties.getProperty("ScreenShotsDirectory");
        String from = FromAddress;//change accordingly
        reportingproperties=null;
        properties.setProperty("mail.smtp.host", SmtpAddress);

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            for (int i = 0; i < To_List.size(); i++) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(To_List.get(i)));
            }
        
            message.setSubject(Subject);

            
         
          //  MimeBodyPart messageBodyPart2 = new MimeBodyPart();

         //   String filename = Attachment_file_path;
           // DataSource source = new FileDataSource(filename);
            //messageBodyPart2.setDataHandler(new DataHandler(source));
            //messageBodyPart2.setFileName(filename.substring(filename.lastIndexOf("\\") + 1, filename.length()));

            Multipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "Click this link for detailed report <a href=" + Jenkins_location + ">    <b>" + Jenkins_location + "</b></a><br><br><br>";
            
            for(int i=0;i<list_of_screenShots.size();i++)
            {
                htmlText=htmlText+"<img src=\"cid:"+list_of_screenShots.get(i)+"\"><br><br>";
            }
                    
            messageBodyPart.setContent(htmlText, "text/html");
            // add it
            multipart.addBodyPart(messageBodyPart);

            // second part (the image)
            
            for(int i=0;i<list_of_screenShots.size();i++)
            {
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource(ScreenShotsDir+list_of_screenShots.get(i));

            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<"+list_of_screenShots.get(i)+">");

            // add image to the multipart
            multipart.addBodyPart(messageBodyPart);
            }
            
            
            for(int i=0;i<html_fileList.size();i++)
            {
              MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            String filename = ScreenShotsDir+html_fileList.get(i);
            DataSource source = new FileDataSource(filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(html_fileList.get(i));
     
            multipart.addBodyPart(messageBodyPart2);
            }

            //6) set the multiplart object to the message object
            message.setContent(multipart);

            //7) send message
            Transport.send(message);

            System.out.println("Sending build report is completed");
        } catch (MessagingException ex) {
            System.out.println("Exception in sending mail" + ex.getMessage());
        }
    }
}
