package com.helfit.wodiary.domain.wsession.service;

import com.helfit.wodiary.domain.exercise.entity.Exercise;
import com.helfit.wodiary.domain.exercise.entity.ExerciseSet;
import com.helfit.wodiary.domain.user.entity.User;
import com.helfit.wodiary.domain.user.repository.UserRepository;
import com.helfit.wodiary.domain.wsession.dto.WsessionDto;
import com.helfit.wodiary.domain.wsession.entity.Wsession;
import com.helfit.wodiary.domain.wsession.repository.WsessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WsessionService {

    private final WsessionRepository wsessionRepository;
    private final UserRepository userRepository;

    /*
    운동세션 생성 로직
     */
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
    /*
    운동세션 불러오는 로직
     */
    public WsessionDto.Response getSession(LocalDate date) {
        Optional<Wsession> sessionOpt = wsessionRepository.findById(date);

        if (!sessionOpt.isPresent()) {
            return null;
        }
        return convertToDto(sessionOpt.get());
    }

    /**
     * Response : date , exercises
     * 세션을 받아서 response 객체로 바꾸는 메서드
     */
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

    /**
     * 세션 삭제 로직
     */
    @Transactional
    public void deleteWsession(LocalDate wsessionId) {
        Wsession wsession = wsessionRepository.findById(wsessionId)
                .orElseThrow(() -> new EntityNotFoundException("Wsession not found with date: " + wsessionId));
        wsessionRepository.delete(wsession);
    }

    /**
     * sourceDate(운동세션이 있는 복사할 날짜)
     * targetDate(운동세션이 없는 붙혀넣을 날짜)
     * 세션 복사 로직
     */
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

    /**
     * Response = date, exercises
     * Resposne 객체로 바꾸는 메서드
     */
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

    /**
     * 해당 달에 운동데이터가 있는 날짜를 반환하는 로직
     * 이 로직을 사용해서 운동데이터가 있는 날짜는 주황색으로 표시함
     */
    public List<LocalDate> getSessionDates(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return wsessionRepository.findAllByWsessionIdBetween(startDate, endDate)
                .stream()
                .map(Wsession::getWsessionId)
                .collect(Collectors.toList());
    }

    /**
     * 한달간 모든 운동세션을 조회해서
     * 모든 운동세션 순회해 총 볼륨을 계산하는 로직
     */
    @Transactional(readOnly = true)
    public WsessionDto.WsessionStatsDto getMonthlyStats(Long userId, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        // 주어진 사용자 ID와 월 기간에 해당하는 모든 운동 세션을 조회
        List<Wsession> sessions = wsessionRepository.findAllByUserIdAndWsessionIdBetween(userId, startDate, endDate);

        int totalVolume = 0;

        // 각 운동 세션을 순회하면서 총 볼륨을 계산
        for (Wsession session : sessions) {
            for (Exercise exercise : session.getExercises()) {
                for (ExerciseSet set : exercise.getSets()) {
                    int setVolume = set.getWeight() * set.getReps();
                    totalVolume += setVolume;
                }
            }
        }
        return new WsessionDto.WsessionStatsDto(totalVolume);
    }
}
