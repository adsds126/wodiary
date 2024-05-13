package com.helfit.wodiary.domain.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ExerciseSetDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddSets {
        private int weight;
        private int reps;
    }
}
