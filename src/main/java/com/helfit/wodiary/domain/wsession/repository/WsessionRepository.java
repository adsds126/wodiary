package com.helfit.wodiary.domain.wsession.repository;

import com.helfit.wodiary.domain.wsession.entity.Wsession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WsessionRepository extends JpaRepository<Wsession, LocalDate> {

}
