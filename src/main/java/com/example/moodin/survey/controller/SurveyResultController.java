package com.example.moodin.survey.controller;

import com.example.moodin.survey.entity.SurveyResultEntity;
import com.example.moodin.survey.service.SurveyResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/survey-result")
public class SurveyResultController {

    private final SurveyResultService surveyResultService;

    public SurveyResultController(SurveyResultService surveyResultService) {
        this.surveyResultService = surveyResultService;
    }

    @PostMapping("/{userId}/{surveyId}")
    public ResponseEntity<SurveyResultEntity> submitSurveyResult(@PathVariable Long userId,@PathVariable Long surveyId) {
        SurveyResultEntity surveyResult = surveyResultService.saveSurveyResult(userId, surveyId);
        return ResponseEntity.ok(surveyResult);

    }
}
