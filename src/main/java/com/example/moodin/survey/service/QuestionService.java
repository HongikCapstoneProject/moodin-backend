package com.example.moodin.survey.service;

import com.example.moodin.survey.entity.QuestionEntity;
import com.example.moodin.survey.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    // 설문 문항 조회
    public List<QuestionEntity> getQuestionBySurveyId(Long surveyId) {
        return questionRepository.findBySurveySurveyId(surveyId);
    }


}
