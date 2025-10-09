package com.example.moodin.survey.repository;

import com.example.moodin.survey.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {
    List<AnswerEntity> findByUser_UserId(Long userId);
    List<AnswerEntity> findByQuestion_QuestionId(Long questionId);

    List<AnswerEntity> findByUserUserIdAndQuestionSurveySurveyId(Long userId, Long surveyId);
}
