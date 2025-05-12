// org.os.minisocial.group.repository/GroupMembershipRequestRepository.java
package org.os.minisocial.group.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.os.minisocial.group.entity.GroupMembershipRequest;
import java.util.List;

@Stateless
public class GroupMembershipRequestRepository {
    @PersistenceContext(unitName = "minisocialPU")
    private EntityManager em;

    public GroupMembershipRequest save(GroupMembershipRequest request) {
        em.persist(request);
        return request;
    }

    public GroupMembershipRequest findById(Long id) {
        return em.find(GroupMembershipRequest.class, id);
    }

    public GroupMembershipRequest update(GroupMembershipRequest request) {
        return em.merge(request);
    }

    public List<GroupMembershipRequest> findByGroupAndStatus(Long groupId, GroupMembershipRequest.RequestStatus status) {
        return em.createQuery(
                        "SELECT r FROM GroupMembershipRequest r WHERE r.group.id = :groupId AND r.status = :status",
                        GroupMembershipRequest.class)
                .setParameter("groupId", groupId)
                .setParameter("status", status)
                .getResultList();
    }

    public GroupMembershipRequest findByUserAndGroup(Long userId, Long groupId) {
        try {
            return em.createQuery(
                            "SELECT r FROM GroupMembershipRequest r WHERE r.user.id = :userId AND r.group.id = :groupId",
                            GroupMembershipRequest.class)
                    .setParameter("userId", userId)
                    .setParameter("groupId", groupId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}