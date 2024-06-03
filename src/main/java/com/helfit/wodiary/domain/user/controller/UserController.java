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
}
