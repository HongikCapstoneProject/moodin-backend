package com.example.moodin.survey.controller;

import com.example.moodin.survey.entity.QuestionEntity;
import com.example.moodin.survey.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    // 설문 문항 조회
    @GetMapping("/survey/{surveyId}")
    public List<QuestionEntity> getQuestions(@PathVariable Long surveyId) {
        return questionService.getQuestionBySurveyId(surveyId);
    }


}
