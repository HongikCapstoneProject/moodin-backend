package com.example.moodin.stress.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StressLevel {
    STABLE(0, "안정"),
    CAUTION(1, "주의"),
    HIGH(2, "높음"),
    CRITICAL(3, "위험");

    private final int code;
    private final String description;

    StressLevel(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    // FastAPI에서 숫자로 받을 때 자동 변환
    @JsonCreator
    public static StressLevel fromCode(int code) {
        for (StressLevel level : StressLevel.values()) {
            if (level.code == code) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid stress level code: " + code);
    }

    // 문자열로도 받을 수 있게 (유연성)
    @JsonCreator
    public static StressLevel fromString(String value) {
        try {
            // 숫자인 경우
            return fromCode(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            // 문자열인 경우 (STABLE, CAUTION 등)
            return StressLevel.valueOf(value.toUpperCase());
        }
    }
}
