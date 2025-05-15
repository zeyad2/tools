package org.os.minisocial.user.repository;

import org.os.minisocial.user.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Stateless
public class UserRepository {
    @PersistenceContext
    private EntityManager em;

    public Optional<User> findByEmail(String email) {
        try {
            User user = em.createNamedQuery("User.findByEmail", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    // In UserRepository.java
    public List<User> searchByNameOrEmail(String query) {
        return em.createQuery("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(:query) OR LOWER(u.email) LIKE LOWER(:query)", User.class)
                .setParameter("query", "%" + query + "%")
                .getResultList();
    }


    public void save(User user) {
        em.persist(user);
    }

    public void update(User user) {
        em.merge(user);
    }

    public void delete(User user) {
        em.remove(em.contains(user) ? user : em.merge(user));
    }
}