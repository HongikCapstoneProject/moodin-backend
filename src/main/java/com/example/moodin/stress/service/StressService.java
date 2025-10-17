package com.example.moodin.stress.service;

import com.example.moodin.stress.dto.FastApiResponseDto;
import com.example.moodin.stress.dto.MeasureRequestDto;
import com.example.moodin.stress.dto.StressRecordResponseDto;
import com.example.moodin.stress.entity.*;
import com.example.moodin.stress.repository.StressAnalysisRepository;
import com.example.moodin.stress.repository.StressRecordRepository;
import com.example.moodin.user.entity.UserEntity;
import com.example.moodin.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StressService {

    @Autowired
    private StressRecordRepository recordRepository;

    @Autowired
    private StressAnalysisRepository analysisRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FastApiService fastApiService;

    /**
     * 스트레스 측정 - Spring이 FastAPI 호출 후 저장
     */
    public StressRecordResponseDto measureStress(UUID userId, MeasureRequestDto request) {
        // 1. 사용자 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 2. FastAPI 호출 (분석만!)
        FastApiResponseDto fastApiResponse = fastApiService.analyzeGsr(
                request.getGsrValues(),
                request.getSampleRate()
        );

        // 3. StressRecord 생성
        StressRecord record = StressRecord.builder()
                .user(user)
                .edaValue(calculateMeanValue(request.getGsrValues()))  // 평균값 저장
                .hrvValue(0.0)  // HRV는 나중에 추가
                .measuredAt(LocalDateTime.now())
                .status(MeasurementStatus.COMPLETED)
                .deviceInfo(request.getDeviceInfo())
                .notes(request.getNotes())
                .build();

        recordRepository.save(record);

        // 4. StressAnalysis 생성
        StressLevel edaLevel = StressLevel.fromCode(fastApiResponse.getStressLevel());
        StressLevel hrvLevel = StressLevel.STABLE;  // HRV 없으면 STABLE
        StressLevel totalLevel = calculateTotalLevel(edaLevel, hrvLevel);

        StressAnalysis analysis = StressAnalysis.builder()
                .stressRecord(record)
                .user(user)
                .edaStressLevel(edaLevel)
                .hrvStressLevel(hrvLevel)
                .totalStressLevel(totalLevel)
                .confidenceScore(fastApiResponse.getStressIndex())
                .algorithmVersion("v1.0.0")
                .analyzedAt(LocalDateTime.now())
                .build();

        analysisRepository.save(analysis);

        // 5. 응답 DTO 생성
        return convertToDto(record, analysis);
    }

    /**
     * FastAPI에서 보낸 결과 저장
     */
    public StressRecordResponseDto saveFromFastApi(
            UUID userId,
            com.example.moodin.stress.controller.StressController.SaveStressRequestDto request) {
        
        // 1. 사용자 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 2. StressRecord 생성
        StressRecord record = StressRecord.builder()
                .user(user)
                .edaValue(request.getEdaValue() != null ? request.getEdaValue() : 0.0)
                .hrvValue(0.0)
                .measuredAt(LocalDateTime.now())
                .status(MeasurementStatus.COMPLETED)
                .deviceInfo(request.getDeviceInfo())
                .notes(request.getNotes())
                .build();

        recordRepository.save(record);

        // 3. StressAnalysis 생성
        StressLevel edaLevel = StressLevel.fromCode(request.getEdaStressLevel());
        StressLevel hrvLevel = request.getHrvStressLevel() != null 
                ? StressLevel.fromCode(request.getHrvStressLevel())
                : StressLevel.STABLE;
        StressLevel totalLevel = calculateTotalLevel(edaLevel, hrvLevel);

        StressAnalysis analysis = StressAnalysis.builder()
                .stressRecord(record)
                .user(user)
                .edaStressLevel(edaLevel)
                .hrvStressLevel(hrvLevel)
                .totalStressLevel(totalLevel)
                .confidenceScore(request.getConfidenceScore())
                .algorithmVersion("v1.0.0")
                .analyzedAt(LocalDateTime.now())
                .build();

        analysisRepository.save(analysis);

        // 4. 응답 DTO 생성
        return convertToDto(record, analysis);
    }

    /**
     * 사용자의 측정 기록 목록 조회 (페이징)
     */
    @Transactional(readOnly = true)
    public Page<StressRecordResponseDto> getRecords(UUID userId, Pageable pageable) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Page<StressRecord> records = recordRepository.findByUser(user, pageable);

        return records.map(record -> {
            StressAnalysis analysis = analysisRepository.findByStressRecord(record).orElse(null);
            return convertToDto(record, analysis);
        });
    }

    /**
     * 특정 측정 기록 상세 조회
     */
    @Transactional(readOnly = true)
    public StressRecordResponseDto getRecordById(UUID recordId) {
        StressRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("측정 기록을 찾을 수 없습니다."));

        StressAnalysis analysis = analysisRepository.findByStressRecord(record).orElse(null);

        return convertToDto(record, analysis);
    }

    /**
     * 측정 기록 삭제
     */
    public void deleteRecord(UUID recordId, UUID userId) {
        StressRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("측정 기록을 찾을 수 없습니다."));

        // 본인 확인
        if (!record.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        recordRepository.delete(record);
    }

    /**
     * 사용자 통계 조회
     */
    @Transactional(readOnly = true)
    public StressStatisticsDto getStatistics(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        long totalMeasurements = recordRepository.countByUser(user);
        
        List<StressAnalysis> analyses = analysisRepository.findByUserOrderByAnalyzedAtDesc(user);
        
        // 최근 측정
        StressRecord lastRecord = recordRepository.findFirstByUserOrderByMeasuredAtDesc(user)
                .orElse(null);

        // 평균 스트레스 레벨 계산
        double avgStressLevel = analyses.stream()
                .mapToInt(a -> a.getTotalStressLevel().getCode())
                .average()
                .orElse(0.0);

        return new StressStatisticsDto(
                totalMeasurements,
                lastRecord != null ? lastRecord.getMeasuredAt() : null,
                StressLevel.fromCode((int) Math.round(avgStressLevel))
        );
    }

    // ========== Helper Methods ==========

    /**
     * GSR 값들의 평균 계산
     */
    private Double calculateMeanValue(List<Double> values) {
        return values.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    /**
     * Total Stress Level 계산 (EDA + HRV / 2, 반올림)
     */
    private StressLevel calculateTotalLevel(StressLevel edaLevel, StressLevel hrvLevel) {
        int total = (int) Math.round((edaLevel.getCode() + hrvLevel.getCode()) / 2.0);
        return StressLevel.fromCode(total);
    }

    /**
     * Entity → DTO 변환
     */
    private StressRecordResponseDto convertToDto(StressRecord record, StressAnalysis analysis) {
        StressRecordResponseDto.AnalysisInfo analysisInfo = null;

        if (analysis != null) {
            analysisInfo = StressRecordResponseDto.AnalysisInfo.builder()
                    .resultId(analysis.getResultId())
                    .edaStressLevel(analysis.getEdaStressLevel())
                    .hrvStressLevel(analysis.getHrvStressLevel())
                    .totalStressLevel(analysis.getTotalStressLevel())
                    .confidenceScore(analysis.getConfidenceScore())
                    .algorithmVersion(analysis.getAlgorithmVersion())
                    .analyzedAt(analysis.getAnalyzedAt())
                    .build();
        }

        return StressRecordResponseDto.builder()
                .recordId(record.getRecordId())
                .userId(record.getUser().getUserId())
                .edaValue(record.getEdaValue())
                .hrvValue(record.getHrvValue())
                .measuredAt(record.getMeasuredAt())
                .createdAt(record.getCreatedAt())
                .status(record.getStatus())
                .deviceInfo(record.getDeviceInfo())
                .notes(record.getNotes())
                .analysis(analysisInfo)
                .build();
    }

    // 통계 DTO
    @lombok.Getter
    @lombok.AllArgsConstructor
    public static class StressStatisticsDto {
        private long totalMeasurements;
        private LocalDateTime lastMeasurementAt;
        private StressLevel averageStressLevel;
    }
}
