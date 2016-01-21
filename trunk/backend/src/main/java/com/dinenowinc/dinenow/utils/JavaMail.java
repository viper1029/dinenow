package com.dinenowinc.dinenow.utils;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.*;

public class JavaMail {    
    String d_email = "abhinav.kumar@udaantechnologies.com", //"hiennguyen922@gmail.com",
            d_password = "Abhi@1234", // "nvh03101992",
            d_host = "smtp.gmail.com",
            d_port = "465",
            m_to =  "abhinav.kumar@udaantechnologies.com", //"hiennguyen92@gmail.com",
            m_subject = "Testing",
            m_text = "please click link below to change the password!";
    
    public JavaMail(String to, String content, String link, boolean isCode) {
    	
    	m_to = to;
    	
    	
        Properties props = new Properties();
        props.put("mail.smtp.user", d_email);
        props.put("mail.smtp.host", d_host);
        props.put("mail.smtp.port", d_port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.port", d_port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        try {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);     
            MimeMessage msg = new MimeMessage(session);
            msg.setText(content);
            MimeBodyPart htmlPart = new MimeBodyPart();
            if (isCode) {
            	htmlPart.setContent("<b>"+content+"</b><br/><br/><br/><b>"+link+"</b>", "text/html");
			}else {
				htmlPart.setContent("<b>"+content+"</b><br/><br/><br/><a href='"+link+"'>"+"Please Click Link"+"</a>", "text/html");
			}
            
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);
            msg.setSubject(m_subject);
            msg.setFrom(new InternetAddress(d_email));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(m_to));
            Transport.send(msg);
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }
    
    
    public JavaMail(String to, String content, String link) throws MessagingException {
    	
    	m_to = to;
    	
    	
        Properties props = new Properties();
        props.put("mail.smtp.user", d_email);
        props.put("mail.smtp.host", d_host);
        props.put("mail.smtp.port", d_port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.port", d_port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);     
            MimeMessage msg = new MimeMessage(session);
            msg.setText(m_text);
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent("<b>"+m_text+"</b><br/><br/><br/><b>"+link+"</b>", "text/html");
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);
            msg.setSubject(m_subject);
            msg.setFrom(new InternetAddress(d_email));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(m_to));
            Transport.send(msg);
    
    }
   
  
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(d_email, d_password);
        }
    }
}