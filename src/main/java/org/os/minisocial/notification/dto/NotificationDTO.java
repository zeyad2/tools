package org.os.minisocial.notification.dto;

import java.time.Instant;

public class NotificationDTO {


    public enum EventType {
        FRIEND_REQUEST("Friend Request"),
        POST_LIKE("Post Like"),
        POST_COMMENT("Post Comment"),
        GROUP_JOIN("Group Join");

        private final String displayName;

        EventType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    private EventType eventType; // "FRIEND_REQUEST", "POST_LIKE", etc.
    private Instant timestamp;
    private String sourceUserId; // Who triggered the event
    private String targetUserId; // Who receives the notification
    private String content; // JSON string with event-specific data




    // Constructors, getters, setters
    public NotificationDTO() {}

    public NotificationDTO(EventType eventType, String sourceUserId, String targetUserId, String content) {
        this.eventType = eventType;
        this.timestamp = Instant.now();
        this.sourceUserId = sourceUserId;
        this.targetUserId = targetUserId;
        this.content = content;
    }



    // Add all getters and setters here
    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(String sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEscapedContent() {
        return content != null ? content.replace("\"", "\\\"") : "";
    }

    // Add validation
    public boolean isValid() {
        return eventType != null
                && sourceUserId != null
                && targetUserId != null
                && content != null;
    }
}