package com.example.moodin.auth.service;
import org.springframework.stereotype.Service;

import java.util.*;
        import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    private final Map<String, Long> tokens = new ConcurrentHashMap<>();

    public String generateToken(Long userId) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, userId);
        return token;
    }

    public Long getUserIdFromToken(String token) {
        return tokens.get(token);
    }

    public void revokeToken(String token) {
        tokens.remove(token);
    }
}
