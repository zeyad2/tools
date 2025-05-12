// org.os.minisocial.group.dto/CreateGroupDTO.java
package org.os.minisocial.group.dto;

public class CreateGroupDTO {
    private String name;
    private String description;
    private String type;

    // Getters and setters
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
}