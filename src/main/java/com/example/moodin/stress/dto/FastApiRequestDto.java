package com.example.moodin.stress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FastApiRequestDto {
    @JsonProperty("eda")
    private List<Double> eda;  // GSR 값들
    
    @JsonProperty("sample_rate")
    private Integer sampleRate;
}
