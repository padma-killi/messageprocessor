package com.tcs.ain.mp.manager;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.DataSource;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;


/**
 * A <code>MailManager</code> is a simple SMTP mail client used to send e-mail.
 *
 * @version  2.0, 08/22/2003
 * @author   Padma Killi
 */

public class MailManager {
   
    private static String SMTP_SERVER ="fdaress13.oc.fda.gov";       //System.getProperty("NET_SMTP_SERVER");

    
    private static final String SMTP_MAIL="smtp";
    
    /**
     * Sends e-mail through the default SMTP server.
     *
     * @param to destination of e-mail message.
     * @param from source of e-mail message.
     * @param subject subject of e-mail message.
     * @param message body of e-mail message.
     * @throws Exception
     */
    public static void sendMail(String to, String from, String subject, String message) 
                         throws Exception {
        
        //sendMail(SMTP_SERVER, to, from, subject, message);
        if (ResourceManager.getInstance().getSmtpServerName() != null)
          SMTP_SERVER = ResourceManager.getInstance().getSmtpServerName();

          sendMail(SMTP_SERVER, to, from, subject, message);           
    }
    
    /**
     * Sends e-mail.
     *
     * @param smtpServer SMTP server name of server used to send e-mail. 
     * @param to destination of e-mail message.
     * @param from source of e-mail message.
     * @param subject subject of e-mail message.
     * @param message body of e-mail message.
     * @throws Exception
     * @throws SendFailedException
     * @throws MessagingException
     */
    public static void sendMail(String smtpServer, String to, String from, 
                                String subject, String message) throws Exception,SendFailedException,MessagingException  {
                            
        InternetAddress[] toList = InternetAddress.parse(to);
        
        // create some properties and get the default Session
        //
        Properties props = new Properties();
        //props.put("smtp.appserver2.gnsi.com", smtpServer);
       if (ResourceManager.getInstance().getSmtpServerName() != null)
          SMTP_SERVER = ResourceManager.getInstance().getSmtpServerName();

        props.put("smtp."+SMTP_SERVER, SMTP_SERVER);
        Session session = Session.getDefaultInstance(props, null);
        
        // create a message
        //
        Message newMessage = new MimeMessage(session);
        
        newMessage.setFrom(new InternetAddress(from));
        newMessage.setRecipients(Message.RecipientType.TO, toList);
        newMessage.setSubject(subject);
        newMessage.setSentDate(new Date());

        // Set message contents
        //
        
        newMessage.setText(message);
        
        // Send newMessage
        //
        Transport transport = session.getTransport(SMTP_MAIL);
        transport.connect(SMTP_SERVER, "", "");
        transport.sendMessage(newMessage, toList);
    }

    /**
     * Sends e-mail with attachment.
     *
     * @param smtpServer SMTP server name of server used to send e-mail. 
     * @param to destination of e-mail message.
     * @param from source of e-mail message.
     * @param subject subject of e-mail message.
     * @param message body of e-mail message.
     * @throws Exception
     * @throws SendFailedException
     * @throws MessagingException
     */
    public static void sendMailWithAttachment(String to, String from, 
                                String subject, String message,String fileToAttach) throws Exception,SendFailedException,MessagingException  {

        InternetAddress[] toList = InternetAddress.parse(to);
        InternetAddress[] fromList = InternetAddress.parse(from);
        
        // create some properties and get the default Session
        //
        Properties props = new Properties();
        //props.put("smtp.appserver2.gnsi.com", smtpServer);
       if (ResourceManager.getInstance().getSmtpServerName() != null)
          SMTP_SERVER = ResourceManager.getInstance().getSmtpServerName();

        props.put("smtp."+SMTP_SERVER, SMTP_SERVER);
        Session session = Session.getDefaultInstance(props, null);

        // create a message
        //
        Message newMessage = new MimeMessage(session);
        
        newMessage.setFrom(new InternetAddress(from));
        newMessage.setRecipients(Message.RecipientType.TO, toList);
        newMessage.setSubject(subject);
        newMessage.setSentDate(new Date());
        newMessage.setReplyTo(fromList) ;

        // Create the message part 
        BodyPart messageBodyPart = new MimeBodyPart();

        /********************************/
        // Create your new message part
         messageBodyPart.setContent(message, "text/html");

         // Create a related multi-part to combine the parts
          MimeMultipart multipart = new MimeMultipart("related");
          multipart.addBodyPart(messageBodyPart);

             

        /********************************/
        // Fill the message
        //messageBodyPart.setText(message);

        //Multipart multipart = new MimeMultipart();
        //multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(ResourceManager.getInstance().getHtmlWritePath()+fileToAttach);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(ResourceManager.getInstance().getHtmlWritePath()+fileToAttach);
        multipart.addBodyPart(messageBodyPart);

        // Put parts in message
        newMessage.setContent(multipart);

       // Send newMessage
       Transport transport = session.getTransport(SMTP_MAIL);
       transport.connect(SMTP_SERVER, "", "");
       transport.sendMessage(newMessage, toList);

       System.out.println("Email Message with attachment is sent to the recipient");
     }

    /**
     * Sends e-mail as an embedded HTML Message
     *
     * @param to destination of e-mail message.
     * @param from source of e-mail message.
     * @param subject subject of e-mail message.
     * @param htmlText body of e-mail message.
     * @throws Exception
     * @throws SendFailedException
     * @throws MessagingException
     */
    public static void sendHTMLMail(String to, String from, 
                                String subject, String htmlText) throws Exception,SendFailedException,MessagingException  {

        InternetAddress[] toList = InternetAddress.parse(to);
        
        // create some properties and get the default Session
        //
        Properties props = new Properties();
        //props.put("smtp.appserver2.gnsi.com", smtpServer);
       if (ResourceManager.getInstance().getSmtpServerName() != null)
          SMTP_SERVER = ResourceManager.getInstance().getSmtpServerName();

        props.put("smtp."+SMTP_SERVER, SMTP_SERVER);
        Session session = Session.getDefaultInstance(props, null);

        // create a message
        Message newMessage = new MimeMessage(session);

        //Fill the headers
        newMessage.setFrom(new InternetAddress(from));
        newMessage.setRecipients(Message.RecipientType.TO, toList);
        newMessage.setSubject(subject);
        newMessage.setSentDate(new Date());

       // Put parts in message
        newMessage.setContent(htmlText, "text/html");

       // Send newMessage
       Transport transport = session.getTransport(SMTP_MAIL);
       transport.connect(ResourceManager.getInstance().getSmtpServerName(), "", "");
       transport.sendMessage(newMessage, toList);

     }
    
    public static void main(String[] args) {
        try {
            StringBuffer sb  = new StringBuffer();
            sb.append("This notice has been sent to you because you have been named the US Agent for the")
          .append("<br>")
          .append("facility listed in the attached registration.")
          .append("<br><br>")
          .append("Please review the registration in the attached document and respond to this e-mail using the following links:")
          .append("<br><br>")
          .append("Press")
          .append("<a href=")
          .append("mailto:")
          .append("vbose@gnsi.com")
          .append("?subject=agree")
          .append(",")
          .append(1234)
          .append("&body=Note:%20Please%20do%20not%20modify%20the%20subject%20heading%20in%20this%20e-mail.%20Changing%20the%20subject%20may%20prevent%20FDA%20from%20processing%20your%20response%20correctly,%20and%20may%20impact%20the%20status%20of%20your%20registration.")
          .append("> <b>AGREE</b> </a>")
          .append("<br>")
          .append("if you agree that you are the US Agent for this facility.")
          .append("<br><br>")
          .append("Press")
          .append("<a href=")
          .append("mailto:")
          .append("vbose@gnsi.com")
          .append("?subject=disagree")
          .append(",")
          .append(1234)
          .append("&body=Note:%20Please%20do%20not%20modify%20the%20subject%20heading%20in%20this%20e-mail.%20Changing%20the%20subject%20may%20prevent%20FDA%20from%20processing%20your%20response%20correctly,%20and%20may%20impact%20the%20status%20of%20your%20registration.")
          .append("> <b>DISAGREE</b> </a>")
          .append("if you are not the US Agent for this facility.")
          .append("<br><br>")
          .append("If you need further assistance, please go to:<br>")
          .append("<a href=")
          .append(ResourceManager.getInstance().getFdaHelpDeskUrl())
          .append(">")
          .append(ResourceManager.getInstance().getFdaHelpDeskUrl())
          .append("</a><br><br>")
          .append("Thank You.")
          .append("<br>");
          
            String subject = "Testing java email client by Vigil";
            String message = "<H1>Hello</H1>";
            String message1 = "Please review the attachment and reply whether you agree or not and send mail to vbose@gnsi.com";
            MailManager.sendHTMLMail("vbose@gnsi.com", "vbose@gnsi.com", subject, sb.toString());
            System.out.println(ResourceManager.getInstance().getFdaHelpDeskUrl());
            //MailManager.sendMailWithAttachment("vbose@gnsi.com","vbose@gnsi.com",subject,message1,"registration.html");
        }catch(Exception e) {e.printStackTrace();}
    }
    
}
