package com.example.moodin.survey.controller;


import com.example.moodin.survey.dto.AnswerRequestDto;
import com.example.moodin.survey.dto.AnswerUpdateRequestDto;
import com.example.moodin.survey.entity.AnswerEntity;
import com.example.moodin.survey.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    // 답변 저장
    @PostMapping
    public ResponseEntity<AnswerEntity> saveAnswer(@RequestBody AnswerRequestDto answerRequestDto) {
        AnswerEntity savedAnswer = answerService.saveAnswer(
                answerRequestDto.getUserId(),
                answerRequestDto.getQuestionId(),
                answerRequestDto.getOptionId()
        );
        return ResponseEntity.ok(savedAnswer);
    }

    // 답변 수정
    @PutMapping("/{answerId}")
    public ResponseEntity<AnswerEntity> updateAnswer(
            @PathVariable Long answerId,
            @RequestBody AnswerUpdateRequestDto answerUpdateRequestDto)
    {
        AnswerEntity updatedAnswer = answerService.updateAnswer(answerId, answerUpdateRequestDto.getOptionId());
        return ResponseEntity.ok(updatedAnswer);
    }

    // 사용자별 답변 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AnswerEntity>> getAnswersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(answerService.getAnswerByUser(userId));
    }

    // 특정 문항에 대한 답변 좋회
    @GetMapping("question/{questionId}")
    public ResponseEntity<List<AnswerEntity>> getAnswersByQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(answerService.getAnswerByQuestion(questionId));
    }


}
