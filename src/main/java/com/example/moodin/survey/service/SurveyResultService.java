package com.example.moodin.survey.service;

import com.example.moodin.survey.entity.AnswerEntity;
import com.example.moodin.survey.entity.AnswerOptionEntity;
import com.example.moodin.survey.entity.SurveyEntity;
import com.example.moodin.survey.entity.SurveyResultEntity;
import com.example.moodin.survey.repository.AnswerRepository;
import com.example.moodin.survey.repository.SurveyRepository;
import com.example.moodin.survey.repository.SurveyResultRepository;
import com.example.moodin.user.entity.UserEntity;
import com.example.moodin.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyResultService {

    private final SurveyResultRepository surveyResultRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;

    @Transactional
    public SurveyResultEntity saveSurveyResult(Long userId, Long surveyId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));
        SurveyEntity survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("설문이 존재하지 않습니다."));


        List<AnswerEntity> answers = answerRepository.findByUserUserIdAndQuestionSurveySurveyId(userId,surveyId);

        int totalScore = answers.stream()
                .mapToInt(a -> a.getAnswerOption().getScore())
                .sum();

        String level = calculateLevel(totalScore,survey);

        SurveyResultEntity surveyResult = SurveyResultEntity.builder()
                .user(user)
                .survey(survey)
                .totalScore(totalScore)
                .level(level)
                .build();

        return surveyResultRepository.save(surveyResult);

    }

    private String calculateLevel(int totalScore, SurveyEntity survey) {
        int maxScore = survey.getQuestionList().stream()
                .mapToInt(q -> q.getAnswerOptionList().stream()
                        .mapToInt(AnswerOptionEntity::getScore).max().orElse(0))
                .sum();

        int ratio = totalScore/maxScore;

        if(ratio >= 19) return "매우 높음";
        else if (ratio >= 17) return "높음";
        else if (ratio >= 16) return "보통";
        else return "낮음";


    }

}
