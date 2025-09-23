package com.example.moodin.survey.repository;

import com.example.moodin.survey.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    List<QuestionEntity> findBySurveySurveyId(Long surveyId);
}
