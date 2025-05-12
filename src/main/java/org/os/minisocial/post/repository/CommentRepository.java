package org.os.minisocial.post.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.os.minisocial.post.entity.Comment;
import java.util.List;

@Stateless
public class CommentRepository {
    @PersistenceContext(unitName = "minisocialPU")
    private EntityManager em;

    public Comment save(Comment comment) {
        em.persist(comment);
        return comment;
    }

    public List<Comment> findByPost(Long postId) {
        return em.createNamedQuery("Comment.findByPost", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }
}