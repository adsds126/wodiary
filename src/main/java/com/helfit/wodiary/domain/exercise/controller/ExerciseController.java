package com.helfit.wodiary.domain.exercise.controller;

import com.helfit.wodiary.domain.exercise.dto.ExerciseSetDto;
import com.helfit.wodiary.domain.exercise.entity.Exercise;
import com.helfit.wodiary.domain.exercise.entity.ExerciseSet;
import com.helfit.wodiary.domain.exercise.repository.ExerciseRepository;
import com.helfit.wodiary.domain.exercise.repository.ExerciseSetRepository;
import com.helfit.wodiary.domain.exercise.service.ExerciseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/exercises")
@RequiredArgsConstructor
public class ExerciseController {
    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseSetRepository exerciseSetRepository;

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping("/{exerciseId}/sets")
    public ResponseEntity<?> addExerciseSet(@PathVariable Long exerciseId, @RequestBody ExerciseSetDto.AddSets exerciseSetDto) {
        try {
            ExerciseSet exerciseSet = exerciseService.addExerciseSet(exerciseId, exerciseSetDto);
            return ResponseEntity.ok("세트추가가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding exercise set: " + e.getMessage());
        }
    }

    @PatchMapping("/sets/{setId}")
    public ResponseEntity<ExerciseSetDto.Response> updateExerciseSet(@PathVariable Long setId, @RequestBody ExerciseSetDto.UpdateSets updateDto) {
        try {
            ExerciseSetDto.Response updatedSet = exerciseService.updateExerciseSet(setId, updateDto);
            return ResponseEntity.ok(updatedSet);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long exerciseId) {
        try {
            exerciseService.deleteExercise(exerciseId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting exercise: " + e.getMessage());
        }
    }
}