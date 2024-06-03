package com.helfit.wodiary.domain.exercise.repository;

import com.helfit.wodiary.domain.exercise.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

}
