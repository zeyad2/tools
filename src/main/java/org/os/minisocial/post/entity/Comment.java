package org.os.minisocial.post.entity;

import jakarta.persistence.*;
import org.os.minisocial.user.entity.User;
import java.util.Date;

@Entity
@Table(name = "comments")
@NamedQuery(
        name = "Comment.findByPost",
        query = "SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.createdAt DESC"
)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}