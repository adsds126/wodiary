package com.helfit.wodiary.domain.workoutentry.entity;

import com.helfit.wodiary.domain.exercise.Exercise;
import com.helfit.wodiary.domain.exerciseset.entity.ExerciseSet;
import com.helfit.wodiary.domain.workoutsession.entity.WorkoutSession;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class WorkoutEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private WorkoutSession workoutSession;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @OneToMany(mappedBy = "workoutEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseSet> sets = new ArrayList<>();

    // 생성자, 게터, 세터 생략
}
