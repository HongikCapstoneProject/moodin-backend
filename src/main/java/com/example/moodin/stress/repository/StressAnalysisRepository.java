package com.example.moodin.stress.repository;

import com.example.moodin.stress.entity.StressAnalysis;
import com.example.moodin.stress.entity.StressRecord;
import com.example.moodin.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StressAnalysisRepository extends JpaRepository<StressAnalysis, UUID> {
    
    // 특정 측정 기록의 분석 결과 조회
    Optional<StressAnalysis> findByStressRecord(StressRecord stressRecord);
    
    // 사용자의 모든 분석 결과 조회
    List<StressAnalysis> findByUserOrderByAnalyzedAtDesc(UserEntity user);
    
    // 사용자의 분석 결과 개수
    long countByUser(UserEntity user);
}
