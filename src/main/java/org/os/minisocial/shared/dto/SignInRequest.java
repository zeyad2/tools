package org.os.minisocial.shared.dto;

public class SignInRequest {
    private String email;
    private String password;

    public SignInRequest() {}

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "SignInRequest{email='" + email + "', password='[PROTECTED]'}";
    }
}