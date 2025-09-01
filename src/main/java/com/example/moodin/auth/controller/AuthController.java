package com.example.moodin.auth.controller;

import com.example.moodin.auth.service.TokenService;
import com.example.moodin.user.entity.UserEntity;
import com.example.moodin.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            String token = tokenService.generateToken(userOpt.get().getUserId());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("로그인 실패");
    }

    // 내 정보
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("토큰 필요");
        }
        String token = authHeader.substring(7);
        Long userId = tokenService.getUserIdFromToken(token);
        if (userId == null) return ResponseEntity.status(401).body("잘못된 토큰");

        return ResponseEntity.ok("로그인된 유저 ID: " + userId);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenService.revokeToken(authHeader.substring(7));
        }
        return ResponseEntity.ok("로그아웃 성공");
    }
}

