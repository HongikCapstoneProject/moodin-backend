package com.example.moodin.stress.service;

import com.example.moodin.stress.dto.FastApiRequestDto;
import com.example.moodin.stress.dto.FastApiResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FastApiService {

    @Value("${fastapi.url:http://localhost:8000}")
    private String fastApiUrl;

    private final RestTemplate restTemplate;

    public FastApiService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * FastAPI에 GSR 데이터 전송하고 분석 결과만 받기 (저장은 Spring에서!)
     */
    public FastApiResponseDto analyzeGsr(List<Double> gsrValues, Integer sampleRate) {
        String url = fastApiUrl + "/api/predict_raw";

        // 요청 DTO 생성 (user_id 없이!)
        FastApiRequestDto request = FastApiRequestDto.builder()
                .eda(gsrValues)
                .sampleRate(sampleRate != null ? sampleRate : 20)
                .build();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FastApiRequestDto> entity = new HttpEntity<>(request, headers);

        try {
            // FastAPI 호출
            ResponseEntity<FastApiResponseDto> response = restTemplate.postForEntity(
                    url,
                    entity,
                    FastApiResponseDto.class
            );

            return response.getBody();

        } catch (Exception e) {
            throw new RuntimeException("FastAPI 호출 실패: " + e.getMessage(), e);
        }
    }

    /**
     * FastAPI 서버 상태 확인
     */
    public boolean isHealthy() {
        try {
            String url = fastApiUrl + "/health";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
