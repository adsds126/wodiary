package com.helfit.wodiary.domain.wsession.dto;

import com.helfit.wodiary.domain.exercise.entity.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class WsessionDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Add {
        private LocalDate date;
        private Long userId;
        private List<ExerciseType> exerciseTypes;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private LocalDate date;
        private Long userId;
    }
}
