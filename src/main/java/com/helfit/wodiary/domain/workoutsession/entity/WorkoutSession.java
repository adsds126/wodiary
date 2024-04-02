package com.helfit.wodiary.domain.workoutsession.entity;

import com.helfit.wodiary.domain.user.entity.User;
import com.helfit.wodiary.domain.workoutentry.entity.WorkoutEntry;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class WorkoutSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate date;

    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutEntry> entries = new ArrayList<>();

    // 생성자, 게터, 세터 생략
}
