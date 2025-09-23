package com.example.moodin.survey.repository;

import com.example.moodin.survey.entity.SurveyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<SurveyEntity, Long>{
    List<SurveyEntity> findAllSurvey(); // 모든 설문지 조회 : 스트레스 , 화병
    Optional<SurveyEntity> findBySurveyId(Long surveyId); // 특정 설문 조회
}
