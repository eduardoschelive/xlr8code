package com.xlr8code.server.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPassword {

    @Column(name = "password", nullable = false, length = 60)
    private String encodedPassword;

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
