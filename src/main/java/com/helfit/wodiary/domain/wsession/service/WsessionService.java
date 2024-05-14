package com.helfit.wodiary.domain.wsession.service;

import com.helfit.wodiary.domain.exercise.entity.Exercise;
import com.helfit.wodiary.domain.user.entity.User;
import com.helfit.wodiary.domain.user.repository.UserRepository;
import com.helfit.wodiary.domain.wsession.dto.WsessionDto;
import com.helfit.wodiary.domain.wsession.entity.Wsession;
import com.helfit.wodiary.domain.wsession.repository.WsessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WsessionService {

    @Autowired
    private WsessionRepository wsessionRepository;

    @Autowired
    private UserRepository userRepository;

//    @Transactional
//    public WsessionDto.Response createSession(WsessionDto.Add sessionDto) {
//        User user = userRepository.findById(sessionDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
//        Wsession session = new Wsession();
//        session.setWsessionId(sessionDto.getDate());
//        session.setUser(user);
//        session.setExercises(sessionDto.getExerciseTypes().stream()
//                .map(type -> {
//                    Exercise exercise = new Exercise();
//                    exercise.setWsession(session);
//                    exercise.setType(type);
//                    exercise.setSets(new ArrayList<>()); // 초기 세트 없이 생성
//                    return exercise;
//                }).collect(Collectors.toList()));
//        wsessionRepository.save(session);
//
//        return new WsessionDto.Response(session.getWsessionId(),user.getId(), session.getExercises());
//    }
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
                                .map(set -> new WsessionDto.SetDetails(set.getExercise().getExerciseId(), set.getWeight(), set.getReps()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new WsessionDto.Response(session.getWsessionId(), user.getId(), exerciseDetails);
    }
    public WsessionDto.Response getSession(LocalDate date) {
        Wsession session = wsessionRepository.findById(date)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        return convertToDto(session);
    }

    private WsessionDto.Response convertToDto(Wsession session) {
        List<WsessionDto.ExerciseDetails> exercises = session.getExercises().stream()
                .map(exercise -> new WsessionDto.ExerciseDetails(
                        exercise.getExerciseId(),
                        exercise.getType(),
                        exercise.getSets().stream()
                                .map(set -> new WsessionDto.SetDetails(set.getSetId(), set.getWeight(), set.getReps()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new WsessionDto.Response(session.getWsessionId(), session.getUser().getId(), exercises);
    }

}
