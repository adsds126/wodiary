package com.helfit.wodiary.domain.wsession.service;

import com.helfit.wodiary.domain.exercise.entity.Exercise;
import com.helfit.wodiary.domain.exercise.entity.ExerciseSet;
import com.helfit.wodiary.domain.user.entity.User;
import com.helfit.wodiary.domain.user.repository.UserRepository;
import com.helfit.wodiary.domain.wsession.dto.WsessionDto;
import com.helfit.wodiary.domain.wsession.entity.Wsession;
import com.helfit.wodiary.domain.wsession.repository.WsessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WsessionService {

    @Autowired
    private WsessionRepository wsessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public WsessionDto.Response createSession(WsessionDto.Add sessionDto) {
        User user = userRepository.findById(sessionDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Wsession session = new Wsession();
        session.setWsessionId(sessionDto.getDate());
        session.setUser(user);
        List<Exercise> exercises = sessionDto.getExerciseTypes().stream()
                .map(type -> {
                    Exercise exercise = new Exercise();
                    exercise.setWsession(session);
                    exercise.setType(type);
                    exercise.setSets(new ArrayList<>()); // 초기 세트 없이 생성
                    return exercise;
                }).collect(Collectors.toList());
        session.setExercises(exercises);
        wsessionRepository.save(session);

        List<WsessionDto.ExerciseDetails> exerciseDetails = exercises.stream()
                .map(exercise -> new WsessionDto.ExerciseDetails(
                        exercise.getExerciseId(),
                        exercise.getType(),
                        exercise.getSets().stream()
                                .map(set -> new WsessionDto.SetDetails(set.getExercise().getExerciseId(), set.getWeight(), set.getReps(),set.getSetOrder()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new WsessionDto.Response(session.getWsessionId(), exerciseDetails);
    }
    public WsessionDto.Response getSession(LocalDate date) {
        Optional<Wsession> sessionOpt = wsessionRepository.findById(date);

        if (!sessionOpt.isPresent()) {
            return null;
        }
        return convertToDto(sessionOpt.get());
    }

    private WsessionDto.Response convertToDto(Wsession session) {
        List<WsessionDto.ExerciseDetails> exercises = session.getExercises().stream()
                .map(exercise -> new WsessionDto.ExerciseDetails(
                        exercise.getExerciseId(),
                        exercise.getType(),
                        exercise.getSets().stream()
                                .map(set -> new WsessionDto.SetDetails(set.getSetId(), set.getWeight(), set.getReps(), set.getSetOrder()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new WsessionDto.Response(session.getWsessionId(), exercises);
    }

    @Transactional
    public void deleteWsession(LocalDate wsessionId) {
        Wsession wsession = wsessionRepository.findById(wsessionId)
                .orElseThrow(() -> new EntityNotFoundException("Wsession not found with date: " + wsessionId));
        wsessionRepository.delete(wsession);
    }
    @Transactional
    public WsessionDto.Response copyWsession(LocalDate sourceDate, LocalDate targetDate) {
        Wsession sourceSession = wsessionRepository.findById(sourceDate)
                .orElseThrow(() -> new EntityNotFoundException("Source Wsession not found"));

        Wsession newSession = new Wsession();
        newSession.setWsessionId(targetDate);
        newSession.setUser(sourceSession.getUser());  // Assuming the same user

        List<Exercise> copiedExercises = sourceSession.getExercises().stream()
                .map(exercise -> {
                    Exercise newExercise = new Exercise();
                    newExercise.setType(exercise.getType());
                    newExercise.setWsession(newSession);
                    newExercise.setSets(exercise.getSets().stream().map(set -> {
                        ExerciseSet newSet = new ExerciseSet();
                        newSet.setWeight(set.getWeight());
                        newSet.setReps(set.getReps());
                        newSet.setExercise(newExercise);
                        newSet.setSetOrder(set.getSetOrder());
                        return newSet;
                    }).collect(Collectors.toList()));
                    return newExercise;
                }).collect(Collectors.toList());

        newSession.setExercises(copiedExercises);
        wsessionRepository.save(newSession);

        return convertToResponseDto(newSession);
    }

    private WsessionDto.Response convertToResponseDto(Wsession session) {
        List<WsessionDto.ExerciseDetails> exerciseDetails = session.getExercises().stream()
                .map(exercise -> new WsessionDto.ExerciseDetails(
                        exercise.getExerciseId(),
                        exercise.getType(),
                        exercise.getSets().stream()
                                .map(set -> new WsessionDto.SetDetails(set.getExercise().getExerciseId(), set.getWeight(), set.getReps(), set.getSetOrder()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new WsessionDto.Response(session.getWsessionId(), exerciseDetails);
    }
    public List<LocalDate> getSessionDates(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return wsessionRepository.findAllByWsessionIdBetween(startDate, endDate)
                .stream()
                .map(Wsession::getWsessionId)
                .collect(Collectors.toList());
    }
}
