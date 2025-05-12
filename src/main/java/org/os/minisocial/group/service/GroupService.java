// org.os.minisocial.group.service/GroupService.java
package org.os.minisocial.group.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.os.minisocial.group.dto.CreateGroupDTO;
import org.os.minisocial.group.dto.GroupDTO;
import org.os.minisocial.group.dto.GroupMembershipRequestDTO;
import org.os.minisocial.group.entity.Group;
import org.os.minisocial.group.entity.GroupMembershipRequest;
import org.os.minisocial.group.repository.GroupMembershipRequestRepository;
import org.os.minisocial.group.repository.GroupRepository;
import org.os.minisocial.shared.dto.UserDTO;
import org.os.minisocial.user.entity.User;
import org.os.minisocial.user.service.UserService;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class GroupService {
    @EJB
    private GroupRepository groupRepository;

    @EJB
    private GroupMembershipRequestRepository membershipRequestRepository;

    @EJB
    private UserService userService;

    public GroupDTO createGroup(String userEmail, CreateGroupDTO createGroupDTO) {
        User creator = userService.getUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Group group = new Group();
        group.setName(createGroupDTO.getName());
        group.setDescription(createGroupDTO.getDescription());
        group.setType(Group.GroupType.valueOf(createGroupDTO.getType().toUpperCase()));
        group.setCreator(creator);

        // Add creator as member
        Set<User> members = new HashSet<>();
        members.add(creator);
        group.setMembers(members);

        group = groupRepository.save(group);
        return convertToDTO(group);
    }

    public List<GroupDTO> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public GroupDTO getGroupById(Long id) {
        Group group = groupRepository.findById(id);
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }
        return convertToDTO(group);
    }

    public void deleteGroup(Long groupId, String userEmail) {
        Group group = groupRepository.findById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }

        User user = userService.getUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!group.getCreator().getId().equals(user.getId())) {
            throw new SecurityException("Only group admin can delete the group");
        }

        groupRepository.delete(groupId);
    }

    public GroupMembershipRequestDTO joinGroup(Long groupId, String userEmail) {
        Group group = groupRepository.findById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }

        User user = userService.getUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (group.getMembers().contains(user)) {
            throw new IllegalStateException("User is already a member of this group");
        }

        if (group.getType() == Group.GroupType.OPEN) {
            group.getMembers().add(user);
            groupRepository.update(group);
            return null; // No request needed for open groups
        } else {
            // For closed groups, create a membership request
            GroupMembershipRequest existingRequest = membershipRequestRepository
                    .findByUserAndGroup(user.getId(), groupId);

            if (existingRequest != null) {
                throw new IllegalStateException("Membership request already exists");
            }

            GroupMembershipRequest request = new GroupMembershipRequest();
            request.setUser(user);
            request.setGroup(group);
            request.setStatus(GroupMembershipRequest.RequestStatus.PENDING);

            request = membershipRequestRepository.save(request);
            return convertToRequestDTO(request);
        }
    }

    public void leaveGroup(Long groupId, String userEmail) {
        Group group = groupRepository.findById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }

        User user = userService.getUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!group.getMembers().contains(user)) {
            throw new IllegalStateException("User is not a member of this group");
        }

        if (group.getCreator().getId().equals(user.getId())) {
            throw new IllegalStateException("Group admin cannot leave the group");
        }

        group.getMembers().remove(user);
        groupRepository.update(group);
    }

    public List<GroupMembershipRequestDTO> getPendingRequests(Long groupId, String adminEmail) {
        Group group = groupRepository.findById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }

        User admin = userService.getUserByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!group.getCreator().getId().equals(admin.getId())) {
            throw new SecurityException("Only group admin can view membership requests");
        }

        return membershipRequestRepository
                .findByGroupAndStatus(groupId, GroupMembershipRequest.RequestStatus.PENDING)
                .stream()
                .map(this::convertToRequestDTO)
                .collect(Collectors.toList());
    }

    public GroupMembershipRequestDTO processRequest(
            Long requestId,
            String adminEmail,
            boolean approve
    ) {
        GroupMembershipRequest request = membershipRequestRepository.findById(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Request not found");
        }

        User admin = userService.getUserByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!request.getGroup().getCreator().getId().equals(admin.getId())) {
            throw new SecurityException("Only group admin can process membership requests");
        }

        if (request.getStatus() != GroupMembershipRequest.RequestStatus.PENDING) {
            throw new IllegalStateException("Request has already been processed");
        }

        if (approve) {
            request.setStatus(GroupMembershipRequest.RequestStatus.APPROVED);
            request.getGroup().getMembers().add(request.getUser());
            groupRepository.update(request.getGroup());
        } else {
            request.setStatus(GroupMembershipRequest.RequestStatus.REJECTED);
        }

        request = membershipRequestRepository.update(request);
        return convertToRequestDTO(request);
    }

    private GroupDTO convertToDTO(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        dto.setType(group.getType().name());
        dto.setCreatedAt(group.getCreatedAt());

        UserDTO creatorDTO = new UserDTO();
        creatorDTO.setEmail(group.getCreator().getEmail());
        creatorDTO.setName(group.getCreator().getName());
        creatorDTO.setBio(group.getCreator().getBio());
        dto.setCreator(creatorDTO);

        Set<UserDTO> memberDTOs = group.getMembers().stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(user.getEmail());
            userDTO.setName(user.getName());
            userDTO.setBio(user.getBio());
            return userDTO;
        }).collect(Collectors.toSet());
        dto.setMembers(memberDTOs);

        return dto;
    }

    private GroupMembershipRequestDTO convertToRequestDTO(GroupMembershipRequest request) {
        GroupMembershipRequestDTO dto = new GroupMembershipRequestDTO();
        dto.setId(request.getId());
        dto.setGroupId(request.getGroup().getId());
        dto.setStatus(request.getStatus().name());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(request.getUser().getEmail());
        userDTO.setName(request.getUser().getName());
        userDTO.setBio(request.getUser().getBio());
        dto.setUser(userDTO);

        return dto;
    }
}