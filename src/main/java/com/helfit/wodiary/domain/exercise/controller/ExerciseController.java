package com.helfit.wodiary.domain.exercise.controller;

import com.helfit.wodiary.domain.exercise.dto.ExerciseSetDto;
import com.helfit.wodiary.domain.exercise.entity.ExerciseSet;
import com.helfit.wodiary.domain.exercise.service.ExerciseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping("/{exerciseId}/sets")
    public ResponseEntity<?> addExerciseSet(@PathVariable Long exerciseId, @RequestBody ExerciseSetDto.AddSets exerciseSetDto) {
        try {
            System.out.println("Received request to add set for exerciseId: " + exerciseId);
            ExerciseSet exerciseSet = exerciseService.addExerciseSet(exerciseId, exerciseSetDto);

            // JSON 응답 생성
            Map<String, String> response = new HashMap<>();
            response.put("message", "세트추가가 완료되었습니다.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error adding exercise set: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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
