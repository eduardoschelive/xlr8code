package com.xlr8code.server.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
@Getter
public class UserPassword {

    @Column(name = "password", nullable = false, length = 60)
    private String encodedPassword;

    protected UserPassword() {
    }

    public UserPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        this.encodedPassword = passwordEncoder.encode(rawPassword);
    }

    public void setEncodedPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        this.encodedPassword = passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.encodedPassword);
    }

}
