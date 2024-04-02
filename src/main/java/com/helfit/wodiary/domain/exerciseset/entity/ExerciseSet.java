package com.helfit.wodiary.domain.exerciseset.entity;

import com.helfit.wodiary.domain.workoutentry.entity.WorkoutEntry;
import jakarta.persistence.*;

@Entity
public class ExerciseSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private WorkoutEntry workoutEntry;

    private double weight;
    private int repetitions;

    // 생성자, 게터, 세터 생략
}
