package com.helfit.wodiary.domain.user.service;

import com.helfit.wodiary.domain.user.controller.UserController;
import com.helfit.wodiary.domain.user.dto.UserDto;
import com.helfit.wodiary.domain.user.entity.User;
import com.helfit.wodiary.domain.user.repository.UserRepository;
import com.helfit.wodiary.exception.BusinessLogicException;
import com.helfit.wodiary.exception.ExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public User signup(UserDto.Signup signupDto) {
        if (userRepository.findByUsername(signupDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(signupDto.getUsername());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setEmail(signupDto.getEmail());

        return userRepository.save(user);
    }
//    @Transactional
//    public User signup(User user){
//        //이미 가입된 이메일인지 검증
//        verifyExistsUserEmail(user.getEmail());
//        //비밀번호 암호화해서 저장
//        String encryptedPassword = passwordEncoder.encode(user.getPassword());
//        User newUser = new User();
//        newUser.setEmail(user.getEmail());
//        newUser.setPassword(encryptedPassword);
//        newUser.setUsername(user.getUsername());
//        return userRepository.save(newUser);
//    }


    @Transactional(readOnly = true)
    public void verifyExistsUserEmail(String email) {
        logger.info("Verifying if email already exists: {}", email);
        userRepository.findByEmail(email).ifPresent((e) -> {
            logger.error("Email already exists: {}", email);
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_EMAIL);
        });
    }
}
