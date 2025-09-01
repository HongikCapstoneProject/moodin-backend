package com.example.moodin.auth.controller;

import com.example.moodin.auth.dto.SignUpRequestDto;
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

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequestDto req) {
        // 매우 단순 검증 (빈 값 방지 정도)
        if (req.username() == null || req.username().isBlank() ||
                req.password() == null || req.password().isBlank()) {
            return ResponseEntity.badRequest().body("username/password 필요");
        }

        if (userRepository.existsByUsername(req.username())) {
            return ResponseEntity.status(409).body("이미 존재하는 username");
        }

        UserEntity saved = userRepository.save(
                UserEntity.builder()
                        .username(req.username())
                        .password(req.password()) // ⚠️ 평문 저장(빠른 테스트용)
                        .build()
        );

        // 가입 직후 바로 로그인 토큰까지 주고 싶다면 ↓ 주석 해제
        // String token = tokenService.generateToken(saved.getId());
        // return ResponseEntity.status(201).body(token);

        return ResponseEntity.status(201).body("가입 성공");
    }

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

