package org.os.minisocial.post.dto;

import java.util.List;

public class PostDTO {
    private String content;
    private List<String> imageUrls;
    private List<String> links;

    // Getters and Setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    public List<String> getLinks() { return links; }
    public void setLinks(List<String> links) { this.links = links; }
}