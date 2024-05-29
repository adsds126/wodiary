package com.helfit.wodiary.domain.authority.entity;

import com.helfit.wodiary.domain.user.entity.User;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String authority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String getAuthority() {
        return authority;
    }

    // getters and setters
}
