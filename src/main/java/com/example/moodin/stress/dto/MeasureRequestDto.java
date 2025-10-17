package com.example.moodin.stress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeasureRequestDto {
    private List<Double> gsrValues;  // GSR 원시 데이터
    private Integer sampleRate;      // 샘플링 레이트 (default: 20)
    private String deviceInfo;       // 기기 정보
    private String notes;            // 메모
}
