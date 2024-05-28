package com.helfit.wodiary.domain.user.controller;

//import com.helfit.wodiary.domain.user.dto.JwtRequest;
//import com.helfit.wodiary.domain.user.dto.JwtResponse;
import com.helfit.wodiary.domain.user.dto.UserDto;
import com.helfit.wodiary.domain.user.entity.User;
//import com.helfit.wodiary.domain.user.mapper.UserMapper;
import com.helfit.wodiary.domain.user.service.UserService;
//import com.helfit.wodiary.domain.user.util.JwtTokenUtil;
import com.helfit.wodiary.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    //private final UserMapper userMapper;
    //private final JwtTokenUtil jwtTokenUtil;
//    private final AuthenticationManager authenticationManager;
//    private final UserDetailsService jwtUserDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("userDto", new UserDto.Signup());
        return "signup";
    }
    @PostMapping("/signup")
    public String signup(@ModelAttribute UserDto.Signup signupDto, Model model) {
        try {
            userService.signup(signupDto);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during signup: " + e.getMessage());
            return "signup";
        }
    }
//    @PostMapping("/login")
//    public String login(@ModelAttribute UserDto.Login loginDto, Model model) {
//        try {
//            UsernamePasswordAuthenticationToken authToken =
//                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
//            Authentication authentication = authenticationManager.authenticate(authToken);
//            return "redirect:/home";
//        } catch (AuthenticationException e) {
//            model.addAttribute("error", "Invalid username or password.");
//            return "login";
//        }
//    }
//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@RequestBody UserDto.Signup requestBody) {
//        logger.info("Signup attempt with email: {}, username: {}", requestBody.getEmail(), requestBody.getUsername());
//        try {
//            userService.signup(userMapper.userDtoSignupToUser(requestBody));
//            return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
//        } catch (BusinessLogicException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
//        }
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
//        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//
//        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//        final String token = jwtTokenUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new JwtResponse(token));
//    }

//    private void authenticate(String username, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
//    }

}
