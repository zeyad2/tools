package org.os.minisocial.post.dto;

import org.os.minisocial.shared.dto.UserDTO;
import jakarta.json.bind.annotation.JsonbProperty;

import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class PostResponseDTO {
    private Long id;
    private String content;

    private List<String> imageUrls = new ArrayList<>();
    private List<String> links = new ArrayList<>();
    private List<CommentResponseDTO> comments = new ArrayList<>();



    private UserDTO author;
    private Date createdAt;
    private Date updatedAt;
    private int likeCount;

    // Explicit no-arg constructor
    public PostResponseDTO() {}

    // Getters with null checks
    public List<String> getImageUrls() {
        if (this.imageUrls == null) {
            this.imageUrls = new ArrayList<>();
        }
        return this.imageUrls;
    }

    public List<String> getLinks() {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        return this.links;
    }

    // Standard getters and setters for other fields

    public List<CommentResponseDTO> getComments() {
        return comments != null ? comments : Collections.emptyList();
    }

    public void setComments(List<CommentResponseDTO> comments) {
        this.comments = comments != null ? comments : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    public void setLinks(List<String> links) { this.links = links; }
    public UserDTO getAuthor() { return author; }
    public void setAuthor(UserDTO author) { this.author = author; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }


}