package com.example.moodin.user.service;

import com.example.moodin.auth.dto.SignUpRequestDto;
import com.example.moodin.global.exception.DuplicateEmailException;
import com.example.moodin.global.exception.InvalidCredentialsException;
import com.example.moodin.global.exception.UserNotFoundException;
import com.example.moodin.user.entity.UserEntity;
import com.example.moodin.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 회원가입
    public UserEntity signup(SignUpRequestDto request) {
        // 이메일 중복 체크
        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            throw new DuplicateEmailException("이미 등록된 이메일입니다.");
        }

        UserEntity user = UserEntity.builder()
                .userName(request.getUserName())
                .userEmail(request.getUserEmail())
                .userPassword(passwordEncoder.encode(request.getUserPassword()))
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    // 로그인
    public UserEntity login(String email, String password) {
        UserEntity user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 마지막 로그인 시간 업데이트
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return user;
    }

    // 사용자 조회
    @Transactional(readOnly = true)
    public UserEntity findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }

    // 이메일로 사용자 조회
    @Transactional(readOnly = true)
    public UserEntity findByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
