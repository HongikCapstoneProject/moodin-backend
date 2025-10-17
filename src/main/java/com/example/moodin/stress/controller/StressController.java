package com.example.moodin.stress.controller;

import com.example.moodin.stress.dto.MeasureRequestDto;
import com.example.moodin.stress.dto.StressRecordResponseDto;
import com.example.moodin.stress.service.StressService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stress")
public class StressController {

    @Autowired
    private StressService stressService;

    @Autowired
    private com.example.moodin.stress.service.PythonScriptService pythonScriptService;

    /**
     * 측정 시작 (Python 스크립트 실행)
     */
    @PostMapping("/start")
    public ResponseEntity<?> startMeasurement(HttpSession session) {
        try {
            String userIdStr = (String) session.getAttribute("userId");
            if (userIdStr == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "로그인이 필요합니다."
                ));
            }

            // Python 스크립트 실행 (측정 시작)
            pythonScriptService.startMeasurement();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "측정이 시작되었습니다.",
                    "data", Map.of(
                            "status", "MEASURING",
                            "userId", userIdStr
                    )
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "측정 시작 실패",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * FastAPI에서 분석 결과 저장 (FastAPI가 호출)
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveStressResult(@RequestBody SaveStressRequestDto request) {
        try {
            // 고정 userId 사용 (테스트용)
            String userId = "92689f3d-a1c0-4a99-9995-87c526ac51c4";
            
            StressRecordResponseDto result = stressService.saveFromFastApi(
                    UUID.fromString(userId),
                    request
            );

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "측정 결과가 저장되었습니다.",
                    "data", result
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "저장 중 오류가 발생했습니다.",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * 스트레스 측정 (Spring → FastAPI → Spring 저장)
     */
    @PostMapping("/measure")
    public ResponseEntity<?> measureStress(
            @RequestBody MeasureRequestDto request,
            HttpSession session) {

        try {
            // 세션에서 userId 가져오기
            String userIdStr = (String) session.getAttribute("userId");
            if (userIdStr == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "로그인이 필요합니다."
                ));
            }

            UUID userId = UUID.fromString(userIdStr);

            // 측정 및 분석 (FastAPI 호출 → 저장)
            StressRecordResponseDto result = stressService.measureStress(userId, request);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "측정이 완료되었습니다.",
                    "data", result
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "측정 중 오류가 발생했습니다.",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * 측정 기록 목록 조회
     */
    @GetMapping("/records")
    public ResponseEntity<?> getRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "measuredAt,desc") String sort,
            HttpSession session) {

        try {
            String userIdStr = (String) session.getAttribute("userId");
            if (userIdStr == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "로그인이 필요합니다."
                ));
            }

            UUID userId = UUID.fromString(userIdStr);

            // Pageable 생성
            String[] sortParams = sort.split(",");
            Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

            Page<StressRecordResponseDto> records = stressService.getRecords(userId, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", records.getContent());
            response.put("pageable", Map.of(
                    "pageNumber", records.getNumber(),
                    "pageSize", records.getSize(),
                    "totalElements", records.getTotalElements(),
                    "totalPages", records.getTotalPages()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "조회 중 오류가 발생했습니다.",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * 측정 기록 상세 조회
     */
    @GetMapping("/records/{recordId}")
    public ResponseEntity<?> getRecord(
            @PathVariable UUID recordId,
            HttpSession session) {

        try {
            String userIdStr = (String) session.getAttribute("userId");
            if (userIdStr == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "로그인이 필요합니다."
                ));
            }

            StressRecordResponseDto record = stressService.getRecordById(recordId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", record
            ));

        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 측정 기록 삭제
     */
    @DeleteMapping("/records/{recordId}")
    public ResponseEntity<?> deleteRecord(
            @PathVariable UUID recordId,
            HttpSession session) {

        try {
            String userIdStr = (String) session.getAttribute("userId");
            if (userIdStr == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "로그인이 필요합니다."
                ));
            }

            UUID userId = UUID.fromString(userIdStr);
            stressService.deleteRecord(recordId, userId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "측정 기록이 삭제되었습니다."
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 사용자 통계 조회
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics(HttpSession session) {
        try {
            String userIdStr = (String) session.getAttribute("userId");
            if (userIdStr == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "로그인이 필요합니다."
                ));
            }

            UUID userId = UUID.fromString(userIdStr);
            StressService.StressStatisticsDto stats = stressService.getStatistics(userId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", stats
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // ========== DTO ==========

    /**
     * FastAPI에서 보내는 저장 요청 DTO
     */
    @lombok.Getter
    @lombok.Setter
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class SaveStressRequestDto {
        private Double edaValue;            // GSR 평균값
        private Integer edaStressLevel;     // EDA 스트레스 레벨 (0~3)
        private Integer hrvStressLevel;     // HRV 스트레스 레벨 (0~3, 기본 0)
        private Double confidenceScore;     // 신뢰도
        private String deviceInfo;          // 기기 정보
        private String notes;               // 메모
    }
}
