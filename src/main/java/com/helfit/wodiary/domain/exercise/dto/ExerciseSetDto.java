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
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateSets {
        private Integer weight;
        private Integer reps;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Long setId;
        private Integer weight;
        private Integer reps;
        private Integer setOrder;
    }
}
