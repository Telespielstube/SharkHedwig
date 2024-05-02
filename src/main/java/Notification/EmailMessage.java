package Notification;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailMessage implements NotificationService {
    @Override
    public void sendMessage() {
        //provide recipient's email ID
        String to = "jakartato@example.com";
        //provide sender's email ID
        String from = "jakartafrom@example.com";
        //provide Mailtrap's username
        final String username = "a094ccae2cfdb3";
        //provide Mailtrap's password
        final String password = "82a851fcf4aa33";
        //provide Mailtrap's host address
        String host = "smtp.mailtrap.io";

        //configure Mailtrap's SMTP server details
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        //create the Session object
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            //create a MimeMessage object
            Message message = new MimeMessage(session);
            //set From email field

            message.setFrom(new InternetAddress(from));

            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("Here comes Jakarta Mail!");

            message.setText("Just discovered that Jakarta Mail is fun and easy to use");

            Transport.send(message);

            System.out.println("Email Message Sent Successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

