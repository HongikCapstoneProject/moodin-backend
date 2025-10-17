package com.example.moodin.auth.controller;

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
@RequestMapping("/api/v1/debug")
public class DebugController {

    @Autowired
    private UserService userService;

    // 세션 확인
    @GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", session.getId());
        result.put("isNew", session.isNew());
        
        Object userIdObj = session.getAttribute("userId");
        Object userEmail = session.getAttribute("userEmail");
        
        result.put("userId_raw", userIdObj);
        result.put("userId_type", userIdObj != null ? userIdObj.getClass().getName() : "null");
        result.put("userEmail", userEmail);
        
        return ResponseEntity.ok(result);
    }

    // 세션에서 사용자 조회 테스트
    @GetMapping("/test-me")
    public ResponseEntity<?> testMe(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object userIdObj = session.getAttribute("userId");
            result.put("step1_getUserId", "OK");
            result.put("userId_value", userIdObj);
            
            if (userIdObj == null) {
                result.put("error", "userId is null in session");
                return ResponseEntity.ok(result);
            }
            
            result.put("step2_checkNull", "OK");
            
            String userIdStr = userIdObj.toString();
            result.put("step3_toString", userIdStr);
            
            UUID userId = UUID.fromString(userIdStr);
            result.put("step4_parseUUID", userId);
            
            UserEntity user = userService.findById(userId);
            result.put("step5_findUser", "OK");
            result.put("user", Map.of(
                "userId", user.getUserId(),
                "userName", user.getUserName(),
                "userEmail", user.getUserEmail()
            ));
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            result.put("error", e.getClass().getName());
            result.put("errorMessage", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(result);
        }
    }

    // 직접 UUID로 사용자 조회
    @GetMapping("/find-user/{userId}")
    public ResponseEntity<?> findUser(@PathVariable String userId) {
        try {
            UUID uuid = UUID.fromString(userId);
            UserEntity user = userService.findById(uuid);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "user", Map.of(
                    "userId", user.getUserId(),
                    "userName", user.getUserName(),
                    "userEmail", user.getUserEmail()
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }
}
