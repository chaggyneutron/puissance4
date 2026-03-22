package com.enseeiht.puissance4.controller;

import com.enseeiht.puissance4.dto.UserDto;
import com.enseeiht.puissance4.entity.User;
import com.enseeiht.puissance4.security.JwtUtil;
import com.enseeiht.puissance4.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        User user = authService.register(
                body.get("username"),
                body.get("email"),
                body.get("password"));
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(Map.of("token", token, "user", UserDto.from(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        User user = authService.login(body.get("email"), body.get("password"));
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(Map.of("token", token, "user", UserDto.from(user)));
    }


    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "healthy"));
    }
}