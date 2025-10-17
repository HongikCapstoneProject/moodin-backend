package com.example.moodin.stress.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class PythonScriptService {

    private static final String PYTHON_PATH = "python";  // 또는 "python3"
    private static final String SCRIPT_PATH = "/Users/munchangju/gsr-stress-api/scripts/serial_autocollect.py";

    /**
     * Python 스크립트 실행 (측정 시작)
     */
    public void startMeasurement() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    PYTHON_PATH,
                    SCRIPT_PATH
            );

            // 작업 디렉토리 설정
            processBuilder.directory(new java.io.File("/Users/munchangju/gsr-stress-api"));

            // 프로세스 시작 (백그라운드)
            Process process = processBuilder.start();

            // 로그 출력 (선택사항)
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[Python] " + line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.println("[INFO] ✅ Python 스크립트 실행 시작: " + SCRIPT_PATH);

        } catch (Exception e) {
            throw new RuntimeException("Python 스크립트 실행 실패: " + e.getMessage(), e);
        }
    }

    /**
     * Python 경로 확인
     */
    public boolean isPythonAvailable() {
        try {
            Process process = Runtime.getRuntime().exec(PYTHON_PATH + " --version");
            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
