package com.example.moodin.stress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FastApiResponseDto {
    private Map<String, Object> features;  // 추출된 특징들
    
    @JsonProperty("stress_level")
    private Integer stressLevel;  // 0~3
    
    @JsonProperty("stress_index")
    private Double stressIndex;
}
