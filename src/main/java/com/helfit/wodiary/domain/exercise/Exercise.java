package com.helfit.wodiary.domain.exercise;

import com.helfit.wodiary.domain.wsession.entity.Wsession;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Wsession wsession;

    @Enumerated(EnumType.STRING)
    private ExerciseType type;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExerciseSet> sets;
}

