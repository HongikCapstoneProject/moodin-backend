package com.example.moodin.survey.controller;

import com.example.moodin.survey.entity.SurveyEntity;
import com.example.moodin.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    // 설문지 조회
    @GetMapping
    public List<SurveyEntity> getAllSurveys() {
        return surveyService.getAllSurveys();
    }

    // 특정 설문지 조회
    @GetMapping("/{surveyId}")
    public SurveyEntity getSurvey(@PathVariable Long surveyId) {
        return surveyService.getBySurveyId(surveyId);
    }
}
