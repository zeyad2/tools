package org.os.minisocial.post.entity;

import jakarta.persistence.*;
import org.os.minisocial.group.entity.Group;
import org.os.minisocial.user.entity.User;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
@NamedQueries({
        @NamedQuery(name = "Post.findByAuthor", query = "SELECT p FROM Post p WHERE p.author = :author ORDER BY p.createdAt DESC"),
        @NamedQuery(name = "Post.findById", query = "SELECT p FROM Post p WHERE p.id = :id")
})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    @CollectionTable(name = "post_images", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    @ElementCollection
    @CollectionTable(name = "post_links", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "link_url")
    private List<String> links;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(nullable = false)
    private int likeCount = 0;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    // Add getter and setter
    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    public List<String> getLinks() { return links; }
    public void setLinks(List<String> links) { this.links = links; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

}