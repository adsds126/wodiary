package com.helfit.wodiary.domain.view;
//import com.helfit.wodiary.domain.user.dto.JwtResponse;
import com.helfit.wodiary.domain.user.dto.UserDto;
import com.helfit.wodiary.domain.user.service.UserService;
//import com.helfit.wodiary.domain.user.util.JwtTokenUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

//    @Autowired
//    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("username") String username,
                        @ModelAttribute("password") String password,
                        Model model) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/home";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/signup")
    public String showSignupPage() {
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



