package com.thai.shiftlogbook.dto;

import com.thai.shiftlogbook.domain.User;

import java.util.UUID;

public class UserResponse {

    private final UUID id;
    private final String username;
    private final String displayName;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
}