package com.helfit.wodiary.domain.user.controller;

import com.helfit.wodiary.domain.user.dto.UserDto;
import com.helfit.wodiary.domain.user.entity.User;
import com.helfit.wodiary.domain.user.mapper.UserMapper;
import com.helfit.wodiary.domain.user.service.UserService;
import com.helfit.wodiary.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto.Signup requestBody) {
        logger.info("Signup attempt with email: {}, username: {}", requestBody.getEmail(), requestBody.getUsername());
        try {
            userService.signup(userMapper.userDtoSignupToUser(requestBody));
            return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
        } catch (BusinessLogicException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

}
