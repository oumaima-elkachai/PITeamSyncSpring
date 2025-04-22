package com.teamsync.userpi.DTO;

import com.teamsync.userpi.entity.RolesUser;
import com.teamsync.userpi.entity.User;
import com.teamsync.userpi.entity.RolesUser;

public class UserResponse {
    private String id;
    private String name;
    private String email;
    private RolesUser role;
    private String telephone;  // e.g., "+1-234-567-890"
    private String photoUrl;

    // Constructor from User entity
    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.telephone = user.getTelephone();
        this.photoUrl = user.getPhotoUrl();
    }

    // Getters (no setters needed for response DTO)
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public RolesUser getRole() { return role; }
    public String getTelephone() { return telephone; }
    public String getPhotoUrl() { return photoUrl; }
}