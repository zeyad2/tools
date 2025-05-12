package org.os.minisocial.post.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.os.minisocial.post.entity.Like;
import org.os.minisocial.user.entity.User;
import org.os.minisocial.post.entity.Post;

@Stateless
public class LikeRepository {
    @PersistenceContext(unitName = "minisocialPU")
    private EntityManager em;

    public Like save(Like like) {
        em.persist(like);
        return like;
    }

    public void delete(Long id) {
        Like like = em.find(Like.class, id);
        if (like != null) {
            em.remove(like);
        }
    }

    public Like findByUserAndPost(User user, Post post) {
        return em.createNamedQuery("Like.findByUserAndPost", Like.class)
                .setParameter("user", user)
                .setParameter("post", post)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}