// org.os.minisocial.group.repository/GroupRepository.java
package org.os.minisocial.group.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.os.minisocial.group.entity.Group;
import java.util.List;

@Stateless
public class GroupRepository {
    @PersistenceContext(unitName = "minisocialPU")
    private EntityManager em;

    public Group save(Group group) {
        em.persist(group);
        return group;
    }

    public Group findById(Long id) {
        return em.find(Group.class, id);
    }

    public List<Group> findAll() {
        return em.createQuery("SELECT g FROM Group g", Group.class).getResultList();
    }

    public Group update(Group group) {
        return em.merge(group);
    }

    public void delete(Long id) {
        Group group = findById(id);
        if (group != null) {
            em.remove(group);
        }
    }

    public List<Group> findByMember(Long userId) {
        return em.createQuery(
                        "SELECT g FROM Group g JOIN g.members m WHERE m.id = :userId", Group.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}