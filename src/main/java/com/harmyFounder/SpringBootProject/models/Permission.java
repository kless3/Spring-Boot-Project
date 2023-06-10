package com.harmyFounder.SpringBootProject.models;

public enum Permission {
    POSTS_READ("posts:read"),
    POSTS_WRITE("posts:write");


    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
