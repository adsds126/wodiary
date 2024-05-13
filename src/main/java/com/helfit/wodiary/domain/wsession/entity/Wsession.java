package com.helfit.wodiary.domain.wsession.entity;

import com.helfit.wodiary.domain.exercise.entity.Exercise;
import com.helfit.wodiary.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "WSESSION")
public class Wsession {
    @Id
    @Column(columnDefinition = "DATE")
    private LocalDate wsessionId;  // 날짜 형식의 ID, 예: "20240503"

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "wsession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Exercise> exercises;

}
