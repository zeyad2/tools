package org.os.minisocial.notification.service;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.logging.Logger;

@Stateless
public class EmailService {
    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Resource(lookup = "java:jboss/mail/Default")
    private Session mailSession;

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            // Enable debugging to see SMTP communication in logs
            mailSession.setDebug(true);

            Message message = new MimeMessage(mailSession);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            logger.info("Email sent successfully to: " + toEmail);
        } catch (MessagingException e) {
            logger.severe("Failed to send email to " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }
}