package com.library.util; // Ensure this matches your package structure

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    public static boolean sendPasswordResetEmail(String recipientEmail, String newPassword) {
        // Configure your library SMTP email credentials
        final String senderEmail = "your.library.app@gmail.com";
        final String appPassword = "your-app-password"; // 16-character Gmail App Password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Library System - Password Reset Request");
            message.setText("Hello,\n\nYour new updated password is: " + newPassword + "\n\nPlease use this to log in to your account.");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}