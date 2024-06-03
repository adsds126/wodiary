package com.helfit.wodiary.domain.exercise.service;

import com.helfit.wodiary.domain.exercise.dto.ExerciseSetDto;
import com.helfit.wodiary.domain.exercise.entity.Exercise;
import com.helfit.wodiary.domain.exercise.entity.ExerciseSet;
import com.helfit.wodiary.domain.exercise.repository.ExerciseRepository;
import com.helfit.wodiary.domain.exercise.repository.ExerciseSetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseSetRepository exerciseSetRepository;

    @Transactional
    public ExerciseSet addExerciseSet(Long exerciseId, ExerciseSetDto.AddSets exerciseSetDto) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));
        int currentSetCount = exercise.getSets().size();
        ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setExercise(exercise);
        exerciseSet.setWeight(exerciseSetDto.getWeight());
        exerciseSet.setReps(exerciseSetDto.getReps());
        exerciseSet.setSetOrder(currentSetCount + 1);
        return exerciseSetRepository.save(exerciseSet);
    }

    @Transactional
    public ExerciseSetDto.Response updateExerciseSet(Long setId, ExerciseSetDto.UpdateSets updateDto) {
        ExerciseSet exerciseSet = exerciseSetRepository.findById(setId)
                .orElseThrow(EntityNotFoundException::new);

        if (updateDto.getWeight() != null) {
            exerciseSet.setWeight(updateDto.getWeight());
        }
        if (updateDto.getReps() != null) {
            exerciseSet.setReps(updateDto.getReps());
        }
        exerciseSetRepository.save(exerciseSet);
        return new ExerciseSetDto.Response(exerciseSet.getSetId(), exerciseSet.getWeight(), exerciseSet.getReps(),exerciseSet.getSetOrder());
    }

    @Transactional
    public void deleteExercise(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found with id: " + exerciseId));

        exerciseRepository.delete(exercise);
    }
}
