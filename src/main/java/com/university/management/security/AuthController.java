package com.university.management.security;

import com.university.management.api.UserResponse;
import com.university.management.user.User;
import com.university.management.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository contextRepository;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          SecurityContextRepository contextRepository, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.contextRepository = contextRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest request,
                              HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Authentication authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password()));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        contextRepository.saveContext(context, httpRequest, httpResponse);
        User user = userRepository.findByUsername(request.username()).orElseThrow();
        return UserResponse.from(user);
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
}
