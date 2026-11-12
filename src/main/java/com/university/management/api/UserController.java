package com.university.management.api;

import com.university.management.user.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> admins() { return byRole(UserRole.ADMIN); }

    @GetMapping("/teachers")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public List<UserResponse> teachers() { return byRole(UserRole.TEACHER); }

    @GetMapping("/students")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public List<UserResponse> students() { return byRole(UserRole.STUDENT); }

    @PostMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createAdmin(@Valid @RequestBody UserRequest request) {
        return create(request, UserRole.ADMIN);
    }

    @PostMapping("/teachers")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<UserResponse> createTeacher(@Valid @RequestBody UserRequest request) {
        return create(request, UserRole.TEACHER);
    }

    @PostMapping("/students")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<UserResponse> createStudent(@Valid @RequestBody UserRequest request) {
        return create(request, UserRole.STUDENT);
    }

    @PutMapping("/{role}/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and #role == 'teachers') or (hasRole('STUDENT') and #role == 'students')")
    public UserResponse update(@PathVariable String role, @PathVariable Long id,
                               @Valid @RequestBody UserRequest request) {
        User user = findRole(id, role);
        user.setName(request.name());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setDepartment(request.department());
        user.setCourse(request.course());
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        return UserResponse.from(users.save(user));
    }

    @DeleteMapping("/{role}/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and #role == 'teachers') or (hasRole('STUDENT') and #role == 'students')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String role, @PathVariable Long id) {
        users.delete(findRole(id, role));
    }

    @GetMapping("/{role}/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and #role == 'teachers') or (hasRole('STUDENT') and #role == 'students')")
    public UserResponse get(@PathVariable String role, @PathVariable Long id) {
        return UserResponse.from(findRole(id, role));
    }

    private List<UserResponse> byRole(UserRole role) {
        return users.findByRole(role).stream().map(UserResponse::from).toList();
    }

    private ResponseEntity<UserResponse> create(UserRequest request, UserRole role) {
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Password is required when creating a user");
        }
        User user = new User(request.name(), request.email(), request.username(),
            passwordEncoder.encode(request.password()), role, request.department(), request.course());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(users.save(user)));
    }

    private User findRole(Long id, String rolePath) {
        UserRole role = switch (rolePath) {
            case "admins" -> UserRole.ADMIN;
            case "teachers" -> UserRole.TEACHER;
            case "students" -> UserRole.STUDENT;
            default -> throw new ResourceNotFoundException("Unknown resource: " + rolePath);
        };
        return users.findById(id).filter(user -> user.getRole() == role)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
