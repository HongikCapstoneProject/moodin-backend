package com.example.moodin.stress.dto;

import com.example.moodin.stress.entity.MeasurementStatus;
import com.example.moodin.stress.entity.StressLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StressRecordResponseDto {
    private UUID recordId;
    private UUID userId;
    private Double edaValue;
    private Double hrvValue;
    private LocalDateTime measuredAt;
    private LocalDateTime createdAt;
    private MeasurementStatus status;
    private String deviceInfo;
    private String notes;
    
    // 분석 결과 (있으면)
    private AnalysisInfo analysis;
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnalysisInfo {
        private UUID resultId;
        private StressLevel edaStressLevel;
        private StressLevel hrvStressLevel;
        private StressLevel totalStressLevel;
        private Double confidenceScore;
        private String algorithmVersion;
        private LocalDateTime analyzedAt;
    }
}
