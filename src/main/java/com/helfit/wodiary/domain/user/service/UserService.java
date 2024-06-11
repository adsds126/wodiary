package com.helfit.wodiary.domain.user.service;

import com.helfit.wodiary.domain.user.dto.UserDto;
import com.helfit.wodiary.domain.user.entity.User;
import com.helfit.wodiary.domain.user.repository.UserRepository;
import com.helfit.wodiary.exception.BusinessLogicException;
import com.helfit.wodiary.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public User signup(UserDto.Signup signupDto) {
        if (userRepository.findByUsername(signupDto.getUsername()).isPresent()) {
            throw new RuntimeException("이미 회원가입되어있는 회원입니다.");
        }

        User user = new User();
        user.setUsername(signupDto.getUsername());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setEmail(signupDto.getEmail());

        return userRepository.save(user);
    }
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다. : " + username));
    }


    @Transactional(readOnly = true)
    public void verifyExistsUserEmail(String email) {
        logger.info("Verifying if email already exists: {}", email);
        userRepository.findByEmail(email).ifPresent((e) -> {
            logger.error("이메일이 이미 존재합니다. : {}", email);
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_EMAIL);
        });
    }
}
