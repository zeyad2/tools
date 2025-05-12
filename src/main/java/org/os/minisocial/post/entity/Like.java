package org.os.minisocial.post.entity;

import jakarta.persistence.*;
import org.os.minisocial.user.entity.User;

import java.util.Date;

@Entity
@Table(name = "likes")
@NamedQuery(
        name = "Like.findByUserAndPost",
        query = "SELECT l FROM Like l WHERE l.user = :user AND l.post = :post"
)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}