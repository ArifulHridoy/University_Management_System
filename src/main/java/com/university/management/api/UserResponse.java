package com.university.management.api;

import com.university.management.user.User;
import com.university.management.user.UserRole;

public record UserResponse(Long id, String name, String email, String username,
                           UserRole role, String department, String course) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getUsername(),
                user.getRole(), user.getDepartment(), user.getCourse());
    }
}
