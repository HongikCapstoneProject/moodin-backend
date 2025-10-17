package com.example.moodin.auth.controller;

import com.example.moodin.auth.dto.LoginRequestDto;
import com.example.moodin.auth.dto.SignUpRequestDto;
import com.example.moodin.user.entity.UserEntity;
import com.example.moodin.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequestDto request) {
        UserEntity user = userService.signup(request);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getUserId());
        data.put("userName", user.getUserName());
        data.put("userEmail", user.getUserEmail());
        if (user.getCreatedAt() != null) {
            data.put("createdAt", user.getCreatedAt());
        }

        return ResponseEntity.status(201).body(Map.of(
                "success", true,
                "message", "회원가입이 완료되었습니다.",
                "data", data
        ));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDto request,
            HttpSession session) {

        UserEntity user = userService.login(request.getUserEmail(), request.getUserPassword());

        // 세션에 사용자 정보 저장
        session.setAttribute("userId", user.getUserId().toString()); // UUID를 String으로 저장
        session.setAttribute("userEmail", user.getUserEmail());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "로그인 성공",
                "data", Map.of(
                        "userId", user.getUserId(),
                        "userName", user.getUserName(),
                        "userEmail", user.getUserEmail()
                )
        ));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "로그아웃되었습니다."
        ));
    }

    // 현재 로그인한 사용자 정보
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        try {
            Object userIdObj = session.getAttribute("userId");

            if (userIdObj == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "로그인이 필요합니다."
                ));
            }

            // String으로 저장된 UUID를 다시 UUID로 변환
            UUID userId = UUID.fromString(userIdObj.toString());
            UserEntity user = userService.findById(userId);

            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", user.getUserId());
            userData.put("userName", user.getUserName());
            userData.put("userEmail", user.getUserEmail());
            userData.put("isActive", user.isActive());
            
            if (user.getCreatedAt() != null) {
                userData.put("createdAt", user.getCreatedAt());
            }
            
            if (user.getLastLoginAt() != null) {
                userData.put("lastLoginAt", user.getLastLoginAt());
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", userData
            ));
            
        } catch (Exception e) {
            e.printStackTrace(); // 로그 출력
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "사용자 정보 조회 중 오류가 발생했습니다.",
                    "error", e.getMessage()
            ));
        }
    }
}
