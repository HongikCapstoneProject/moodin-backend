package com.example.moodin.survey.repository;

import com.example.moodin.survey.entity.SurveyResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyResultRepository extends JpaRepository<SurveyResultEntity, Long> {
    Optional<SurveyResultEntity> findByUserUserIdAndSurvey_SurveyId(Long userId, Long surveyId);
}
