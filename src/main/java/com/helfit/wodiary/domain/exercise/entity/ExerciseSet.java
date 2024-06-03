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
    private int weight;
    @Column
    private int reps;
    @Column
    private int setOrder;
}
