package com.helfit.wodiary.domain.exercise.entity;

import com.helfit.wodiary.domain.exercise.entity.Exercise;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ExerciseSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SetId;

    @ManyToOne
    private Exercise exercise;
    @Column
    private int weight; // 세트의 무게
    @Column
    private int reps;  // 반복 횟수
}
