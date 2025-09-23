package com.example.moodin.survey.service;

import com.example.moodin.survey.entity.SurveyEntity;
import com.example.moodin.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    // 모든 설문지 조회 : 스트레스 / 화병
    public List<SurveyEntity> getAllSurveys() {
        return surveyRepository.findAllSurvey(); // DB에서 모든 설문지 가져오기
    }

    // 특정 설문지 조회
    public SurveyEntity getBySurveyId(Long surveyId) {
        return surveyRepository.findBySurveyId(surveyId)
                .orElseThrow(() -> new RuntimeException("설문지 없음" + surveyId));
    }

}
