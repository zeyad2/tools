package org.os.minisocial.friend.dto;

public class FriendRequestDTO {
    private String requesterEmail;
    private String addresseeEmail;

    // Getters and setters
    public String getRequesterEmail() { return requesterEmail; }
    public void setRequesterEmail(String requesterEmail) { this.requesterEmail = requesterEmail; }
    public String getAddresseeEmail() { return addresseeEmail; }
    public void setAddresseeEmail(String addresseeEmail) { this.addresseeEmail = addresseeEmail; }
}