// org.os.minisocial.group.dto/GroupDTO.java
package org.os.minisocial.group.dto;

import org.os.minisocial.shared.dto.UserDTO;
import java.util.Date;
import java.util.Set;

public class GroupDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private UserDTO creator;
    private Set<UserDTO> members;
    private Date createdAt;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public Set<UserDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<UserDTO> members) {
        this.members = members;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}