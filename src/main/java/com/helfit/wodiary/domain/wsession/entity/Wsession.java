package com.helfit.wodiary.domain.wsession.entity;

import com.helfit.wodiary.domain.exercise.Exercise;
import com.helfit.wodiary.domain.user.entity.User;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Wsession {
    @Id
    private String id;  // 날짜 형식의 ID, 예: "20240503"

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "wsession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Exercise> exercises;

    private Date sessionDate;
}
