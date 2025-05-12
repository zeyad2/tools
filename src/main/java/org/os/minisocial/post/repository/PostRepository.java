package org.os.minisocial.post.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.os.minisocial.post.entity.Post;
import org.os.minisocial.user.entity.User;
import java.util.List;

@Stateless
public class PostRepository {
    @PersistenceContext(unitName = "minisocialPU")
    private EntityManager em;

    public Post save(Post post) {
        em.persist(post);
        return post;
    }

    public Post update(Post post) {
        return em.merge(post);
    }

    public void delete(Long id) {
        Post post = em.find(Post.class, id);
        if (post != null) {
            em.remove(post);
        }
    }

    public Post findById(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findByAuthor(User author) {
        return em.createNamedQuery("Post.findByAuthor", Post.class)
                .setParameter("author", author)
                .getResultList();
    }

    public List<Post> findPostsByUserAndFriends(User user) {
        return em.createQuery(
                        "SELECT p FROM Post p WHERE p.author = :user OR " +
                                "p.author IN (SELECT f.addressee FROM Friendship f WHERE f.requester = :user AND f.status = 'ACCEPTED') OR " +
                                "p.author IN (SELECT f.requester FROM Friendship f WHERE f.addressee = :user AND f.status = 'ACCEPTED') " +
                                "ORDER BY p.createdAt DESC", Post.class)
                .setParameter("user", user)
                .getResultList();
    }

    public List<Post> findAll() {
        return em.createQuery("SELECT p FROM Post p ORDER BY p.createdAt DESC", Post.class)
                .getResultList();
    }

    // org.os.minisocial.post.repository/PostRepository.java (add this method)
    public List<Post> findByGroup(Long groupId) {
        return em.createQuery(
                        "SELECT p FROM Post p WHERE p.group.id = :groupId ORDER BY p.createdAt DESC",
                        Post.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }
}