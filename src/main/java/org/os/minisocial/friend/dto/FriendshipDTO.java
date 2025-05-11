package org.os.minisocial.friend.dto;

import org.os.minisocial.shared.dto.ProfileDTO;
import java.util.Date;

public class FriendshipDTO {
    private ProfileDTO friend;
    private String status;
    private Date since;

    // Getters and setters
    public ProfileDTO getFriend() { return friend; }
    public void setFriend(ProfileDTO friend) { this.friend = friend; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getSince() { return since; }
    public void setSince(Date since) { this.since = since; }
}