package com.helfit.wodiary.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.helfit.wodiary.domain.user.dto.UserDto;
import com.helfit.wodiary.domain.user.repository.UserRepository;
import com.helfit.wodiary.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignupSuccess() {
        UserDto.Signup signupDto = new UserDto.Signup();
        signupDto.setUsername("testuser");
        signupDto.setPassword("password");
        signupDto.setEmail("testuser@example.com");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(com.helfit.wodiary.domain.user.entity.User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        com.helfit.wodiary.domain.user.entity.User user = userService.signup(signupDto);

        assertEquals(signupDto.getUsername(), user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(signupDto.getEmail(), user.getEmail());
    }

    @Test
    public void testSignupUsernameAlreadyExists() {
        UserDto.Signup signupDto = new UserDto.Signup();
        signupDto.setUsername("testuser");
        signupDto.setPassword("password");
        signupDto.setEmail("testuser@example.com");

        com.helfit.wodiary.domain.user.entity.User existingUser = new com.helfit.wodiary.domain.user.entity.User();
        existingUser.setUsername("testuser");

        when(userRepository.findByUsername(signupDto.getUsername())).thenReturn(Optional.of(existingUser));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.signup(signupDto);
        });

        assertEquals("이미 회원가입되어있는 회원입니다.", exception.getMessage());
    }

    @Test
    public void testGetCurrentUserSuccess() {
        String username = "testuser";
        UserDetails userDetails = User.builder()
                .username(username)
                .password("password")
                .roles("USER")
                .build();

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        com.helfit.wodiary.domain.user.entity.User user = new com.helfit.wodiary.domain.user.entity.User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        com.helfit.wodiary.domain.user.entity.User result = userService.getCurrentUser();
        assertEquals(username, result.getUsername());
    }

    @Test
    public void testGetCurrentUserNotFound() {
        String username = "testuser";

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, null));

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.getCurrentUser();
        });

        assertEquals("유저를 찾을 수 없습니다. : " + username, exception.getMessage());
    }

    @Test
    public void testVerifyExistsUserEmailSuccess() {
        String email = "testuser@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        userService.verifyExistsUserEmail(email);
    }
}

