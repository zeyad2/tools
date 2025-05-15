package org.os.minisocial.notification.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.os.minisocial.notification.dto.NotificationDTO;
import org.os.minisocial.notification.service.EmailService;

import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.os.minisocial.notification.dto.NotificationDTO.EventType.*;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup",
                propertyValue = "java:/jms/queue/NotificationsQueue"),
        @ActivationConfigProperty(propertyName = "destinationType",
                propertyValue = "jakarta.jms.Queue")
})
public class NotificationMDB implements MessageListener {
    private static final Logger logger = Logger.getLogger(NotificationMDB.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @EJB
    private EmailService emailService;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String text = ((TextMessage) message).getText();
                logger.info("Processing notification: " + text);

                // Basic JSON parsing (replace with proper JSON library if needed)
                NotificationDTO notification = parseNotification(text);

                if (notification == null || notification.getEventType() == null) {
                    logger.warning("Invalid notification format");
                    return;
                }

                // Process based on event type
                switch(notification.getEventType()) {
                    case FRIEND_REQUEST:
                        sendFriendRequestEmail(notification);
                        break;
                    case POST_LIKE:
                        sendPostLikeEmail(notification);
                        break;
                    case POST_COMMENT:
                        sendPostCommentEmail(notification);
                        break;
                    case GROUP_JOIN:
                        sendGroupJoinEmail(notification);
                        break;
                    default:
                        logger.warning("Unknown event type: " + notification.getEventType());
                }
            }
        } catch (Exception e) {
            logger.severe("Error processing notification: " + e.toString());
            e.printStackTrace();
        }
    }

    private void sendFriendRequestEmail(NotificationDTO notification) {
        try {
            String subject = "New Friend Request on MiniSocial";
            String body = String.format(
                    "Hello,\n\n%s has sent you a friend request!\n\n" +
                            "Log in to MiniSocial to respond.\n\n" +
                            "The MiniSocial Team",
                    notification.getSourceUserId()
            );

            emailService.sendEmail(notification.getTargetUserId(), subject, body);
            logger.info("Friend request email sent to: " + notification.getTargetUserId());
        } catch (Exception e) {
            logger.severe("Failed to send friend request email: " + e.toString());
        }
    }

    private void sendPostLikeEmail(NotificationDTO notification) {
        String subject = "Your post was liked on MiniSocial";
        String body = String.format(
                "Hello,\n\n%s liked your post:\n\n%s\n\n" +
                        "The MiniSocial Team",
                notification.getSourceUserId(),
                notification.getContent() // Could contain post excerpt
        );
        sendEmail(notification.getTargetUserId(), subject, body);
    }

    private void sendPostCommentEmail(NotificationDTO notification) {
        String subject = "New comment on your MiniSocial post";
        String body = String.format(
                "Hello,\n\n%s commented on your post:\n\n%s\n\n" +
                        "The MiniSocial Team",
                notification.getSourceUserId(),
                notification.getContent() // Could contain comment text
        );
        sendEmail(notification.getTargetUserId(), subject, body);
    }

    private void sendGroupJoinEmail(NotificationDTO notification) {
        String subject = "New member joined your group";
        String body = String.format(
                "Hello,\n\n%s has joined your group:\n\n%s\n\n" +
                        "The MiniSocial Team",
                notification.getSourceUserId(),
                notification.getContent() // Could contain group name
        );
        sendEmail(notification.getTargetUserId(), subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            emailService.sendEmail(to, subject, body);
            logger.info("Email sent to: " + to);
        } catch (Exception e) {
            logger.severe("Failed to send email: " + e.toString());
        }
    }

    private NotificationDTO parseNotification(String json) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);

            NotificationDTO notification = new NotificationDTO();
            notification.setEventType(NotificationDTO.EventType.valueOf(rootNode.get("eventType").asText()));
            notification.setSourceUserId(rootNode.get("sourceUserId").asText());
            notification.setTargetUserId(rootNode.get("targetUserId").asText());
            notification.setContent(rootNode.get("content").toString());
            notification.setTimestamp(Instant.parse(rootNode.get("timestamp").asText()));

            return notification;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to parse notification JSON: " + json, e);
            return null;
        }
    }
}
