package com.example.moodin.survey.repository;

import com.example.moodin.survey.entity.AnswerOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerOptionRepository extends JpaRepository<AnswerOptionEntity,Long> {
}
