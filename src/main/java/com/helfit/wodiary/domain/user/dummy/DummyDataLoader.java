package com.helfit.wodiary.domain.user.dummy;

import com.helfit.wodiary.domain.user.entity.User;
import com.helfit.wodiary.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DummyDataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("dummyuser").isEmpty()) {
            User user = new User();
            user.setUsername("test");
            user.setPassword(passwordEncoder.encode("123"));
            user.setEmail("dummyuser@example.com");
            userRepository.save(user);
        }
    }
}

