package com.university.management.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // 1. POST /api/auth/login with wrong credentials → 401
    @Test
    void testLoginWithBadCredentials() throws Exception {
        String badLoginJson = """
                {"username":"nonexistent","password":"wrongpassword"}
                """;
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badLoginJson))
                .andExpect(status().isUnauthorized());
    }

    // 2. GET /api/students as ADMIN → 200
    @Test
    @WithMockUser(roles = "TEACHER")
    void testGetStudentsAsAdminRole() throws Exception {
        mockMvc.perform(get("/api/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // 3. POST /api/students as ADMIN with valid body → 201
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateStudentAsAdmin() throws Exception {
        String newStudentJson = """
                {
                  "name": "Test Student",
                  "email": "student@test.com",
                  "username": "teststudent",
                  "password": "pass123",
                  "department": "CS",
                  "course": "CS101"
                }
                """;
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newStudentJson))
                .andExpect(status().isCreated());
    }
}
