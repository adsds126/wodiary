package com.helfit.wodiary.domain.exercise.repository;

import com.helfit.wodiary.domain.exercise.entity.ExerciseSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseSetRepository extends JpaRepository<ExerciseSet, Long> {
}
