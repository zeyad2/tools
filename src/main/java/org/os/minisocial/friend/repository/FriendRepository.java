package org.os.minisocial.friend.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.os.minisocial.friend.entity.Friendship;
import org.os.minisocial.user.entity.User;
import java.util.List;

@Stateless
public class FriendRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Friendship friendship) {
        em.persist(friendship);
    }

    public Friendship update(Friendship friendship) {
        return em.merge(friendship);
    }

    public Friendship findByUsers(User user1, User user2) {
        try {
            return em.createNamedQuery("Friendship.findByUsers", Friendship.class)
                    .setParameter("user1", user1)
                    .setParameter("user2", user2)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public List<Friendship> findByUserAndStatus(User user, Friendship.Status status) {
        return em.createNamedQuery("Friendship.findByUserAndStatus", Friendship.class)
                .setParameter("user", user)
                .setParameter("status", status)
                .getResultList();
    }
}