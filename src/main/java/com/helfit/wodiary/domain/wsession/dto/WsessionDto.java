package com.helfit.wodiary.domain.wsession.dto;

import com.helfit.wodiary.domain.exercise.entity.Exercise;
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
        private List<ExerciseDetails> exercises;
        @Override
        public String toString() {
            return "Response{" +
                    "date=" + date +
                    ", exercises=" + exercises +
                    '}';
        }
    }
    @Getter
    @Setter
    public static class ExerciseSetDto {
        private Long id;
        private int weight;
        private int reps;
        private int number; // 세트 번호

    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExerciseDetails {
        private Long id;
        private ExerciseType type;
        private List<SetDetails> sets;
        @Override
        public String toString() {
            return "ExerciseDetails{" +
                    "id=" + id +
                    ", type=" + type +
                    ", sets=" + sets +
                    '}';
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SetDetails {
        private Long id;
        private int weight;
        private int reps;
        @Override
        public String toString() {
            return "SetDetails{" +
                    "id=" + id +
                    ", weight=" + weight +
                    ", reps=" + reps +
                    '}';
        }
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WsessionCopyDto {
        private LocalDate sourceDate;
        private LocalDate targetDate;
    }
}
