package com.helfit.wodiary.domain.exercise.entity;

import com.helfit.wodiary.domain.wsession.entity.Wsession;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ExerciseId;

    @ManyToOne
    @JoinColumn(name = "wsession_id")  // Wsession을 참조하는 외래 키
    private Wsession wsession;

    @Enumerated(EnumType.STRING)
    private ExerciseType type;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseSet> sets;
}

