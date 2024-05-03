package com.helfit.wodiary.domain.exercise;

import jakarta.persistence.*;

@Entity
public class ExerciseSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Exercise exercise;

    private int weight;       // 세트의 무게
    private int repetitions;  // 반복 횟수
}
