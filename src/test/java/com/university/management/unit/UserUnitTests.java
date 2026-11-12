package com.university.management.unit;

import com.university.management.api.ResourceNotFoundException;
import com.university.management.api.UserRequest;
import com.university.management.api.UserResponse;
import com.university.management.user.User;
import com.university.management.user.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserUnitTests {

    // 1. Test User constructor and all getters
    @Test
    void testUserConstructorAndGetters() {
        User user = new User("Alice Smith", "alice@uni.edu", "alice", "secret", UserRole.STUDENT, "CS", "CS101");
        assertEquals("Alice Smith", user.getName());
        assertEquals("alice@uni.edu", user.getEmail());
        assertEquals("alice", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals(UserRole.STUDENT, user.getRole());
        assertEquals("CS", user.getDepartment());
        assertEquals("CS101", user.getCourse());
        // id is null before persistence
        assertNull(user.getId());
    }

    // 2. Test all User setters
    @Test
    void testUserSetters() {
        User user = new User("Bob", "bob@uni.edu", "bob", "pass", UserRole.TEACHER, "Math", "MATH101");
        user.setName("Robert");
        user.setEmail("robert@uni.edu");
        user.setUsername("robert");
        user.setPassword("newpass");
        user.setRole(UserRole.ADMIN);
        user.setDepartment("Physics");
        user.setCourse("PHY101");

        assertEquals("Robert", user.getName());
        assertEquals("robert@uni.edu", user.getEmail());
        assertEquals("robert", user.getUsername());
        assertEquals("newpass", user.getPassword());
        assertEquals(UserRole.ADMIN, user.getRole());
        assertEquals("Physics", user.getDepartment());
        assertEquals("PHY101", user.getCourse());
    }

    // 3. Test UserRole enum has exactly 3 values: ADMIN, TEACHER, STUDENT
    @Test
    void testUserRoleValues() {
        UserRole[] roles = UserRole.values();
        assertEquals(3, roles.length);
        assertEquals(UserRole.ADMIN, roles[0]);
        assertEquals(UserRole.TEACHER, roles[1]);
        assertEquals(UserRole.STUDENT, roles[2]);
    }

    // 4. Test UserResponse.from(User) factory method
    @Test
    void testUserResponseFromUser() {
        User user = new User("Carol", "carol@uni.edu", "carol", "pwd", UserRole.TEACHER, "Biology", "BIO201");
        UserResponse response = UserResponse.from(user);

        assertNull(response.id());   // no persistence, id is null
        assertEquals("Carol", response.name());
        assertEquals("carol@uni.edu", response.email());
        assertEquals("carol", response.username());
        assertEquals(UserRole.TEACHER, response.role());
        assertEquals("Biology", response.department());
        assertEquals("BIO201", response.course());
    }

    // 5. Test ResourceNotFoundException message
    @Test
    void testResourceNotFoundExceptionMessage() {
        String msg = "User not found with id 42";
        ResourceNotFoundException ex = new ResourceNotFoundException(msg);
        assertEquals(msg, ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    // 6. Test UserRequest record fields
    @Test
    void testUserRequestRecord() {
        UserRequest req = new UserRequest("Dave", "dave@uni.edu", "dave", "pass123", "Engineering", "ENG301");
        assertEquals("Dave", req.name());
        assertEquals("dave@uni.edu", req.email());
        assertEquals("dave", req.username());
        assertEquals("pass123", req.password());
        assertEquals("Engineering", req.department());
        assertEquals("ENG301", req.course());
    }
}
