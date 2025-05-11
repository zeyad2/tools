package org.os.minisocial.shared.dto;

public class ProfileDTO {
    private String name;
    private String bio;
    private String email;
    private String newPassword;

    public ProfileDTO() {}

    public ProfileDTO(String name, String bio, String email, String newPassword) {
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.newPassword = newPassword;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "name='" + name + '\'' +
                ", bio='" + bio + '\'' +
                ", email='" + email + '\'' +
                ", newPassword='" + (newPassword != null ? "[PROTECTED]" : "null") + '\'' +
                '}';
    }
}