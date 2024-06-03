package com.helfit.wodiary.domain.wsession.repository;

import com.helfit.wodiary.domain.wsession.entity.Wsession;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WsessionRepository extends JpaRepository<Wsession, LocalDate> {
    @EntityGraph(attributePaths = {"user", "exercises.sets"})
    Optional<Wsession> findById(LocalDate id);

    List<Wsession> findAllByWsessionIdBetween(LocalDate startDate, LocalDate endDate);
}
