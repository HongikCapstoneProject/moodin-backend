package com.example.moodin.stress.repository;

import com.example.moodin.stress.entity.StressRecord;
import com.example.moodin.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StressRecordRepository extends JpaRepository<StressRecord, UUID> {
    
    // 사용자별 기록 조회 (페이징)
    Page<StressRecord> findByUser(UserEntity user, Pageable pageable);
    
    // 사용자별 기록 조회 (리스트)
    List<StressRecord> findByUserOrderByMeasuredAtDesc(UserEntity user);
    
    // 사용자의 특정 기간 기록 조회
    List<StressRecord> findByUserAndMeasuredAtBetweenOrderByMeasuredAtDesc(
        UserEntity user, 
        LocalDateTime startDate, 
        LocalDateTime endDate
    );
    
    // 사용자의 기록 개수
    long countByUser(UserEntity user);
    
    // 사용자의 최근 기록
    Optional<StressRecord> findFirstByUserOrderByMeasuredAtDesc(UserEntity user);
}
