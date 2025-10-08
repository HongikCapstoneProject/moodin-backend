package com.example.moodin.survey.service;

import com.example.moodin.survey.entity.AnswerEntity;
import com.example.moodin.survey.entity.AnswerOptionEntity;
import com.example.moodin.survey.entity.QuestionEntity;
import com.example.moodin.survey.repository.AnswerOptionRepository;
import com.example.moodin.survey.repository.AnswerRepository;
import com.example.moodin.survey.repository.QuestionRepository;
import com.example.moodin.user.entity.UserEntity;
import com.example.moodin.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final UserRepository userRepository;

    // 답변 저장
    public AnswerEntity saveAnswer(Long userId, Long questionId, Long optionId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("사용자가 없습니다."));
        QuestionEntity question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));
        AnswerOptionEntity option = answerOptionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("선택지가 존재하지 않습니다."));

        AnswerEntity answer = new AnswerEntity();
        answer.setUser(user);
        answer.setQuestion(question);
        answer.setAnswerOption(option);

        return answerRepository.save(answer);
    }

    // 답변 수정
    @Transactional
    public AnswerEntity updateAnswer(Long answerId, Long newOptionId) {
        AnswerEntity answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변이 존재하지 않습니다"));
        AnswerOptionEntity newAnswer = answerOptionRepository.findById(newOptionId)
                .orElseThrow(() -> new IllegalArgumentException("선택지가 존재하지 않습니다"));
        answer.setAnswerOption(newAnswer);
        return answer;
    }

    // 사용자별 답변 조회
    public List<AnswerEntity> getAnswerByUser(Long userId){
        return answerRepository.findByUser_UserId(userId);
    }

    // 특정 문항에 대한 답변 조회
    public List<AnswerEntity> getAnswerByQuestion(Long questionId){
        return answerRepository.findByQuestion_QuestionId(questionId);
    }


}
