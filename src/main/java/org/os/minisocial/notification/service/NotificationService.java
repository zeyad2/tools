package org.os.minisocial.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import org.os.minisocial.notification.dto.NotificationDTO;

import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class NotificationService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());


    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "java:/jms/queue/NotificationsQueue")
    private Queue notificationsQueue;

    public void sendNotification(NotificationDTO notification) {
        if (!notification.isValid()) {
            logger.warning("Attempted to send invalid notification");
            return;
        }

        try {
            String json = String.format(
                    "{\"eventType\":\"%s\",\"sourceUserId\":\"%s\",\"targetUserId\":\"%s\",\"content\":%s,\"timestamp\":\"%s\"}",
                    notification.getEventType().name(),
                    notification.getSourceUserId(),
                    notification.getTargetUserId(),
                    notification.getContent(), // Content should already be valid JSON
                    notification.getTimestamp()
            );

            jmsContext.createProducer().send(notificationsQueue, json);
            logger.info("Notification sent successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to send notification", e);
            throw new RuntimeException("Notification failed", e);
        }
    }
}