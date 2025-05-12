package org.os.minisocial.post.dto;

import org.os.minisocial.shared.dto.UserDTO;
import java.util.Date;

public class CommentResponseDTO {
    private Long id;
    private String content;
    private UserDTO author;
    private Date createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public UserDTO getAuthor() { return author; }
    public void setAuthor(UserDTO author) { this.author = author; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}