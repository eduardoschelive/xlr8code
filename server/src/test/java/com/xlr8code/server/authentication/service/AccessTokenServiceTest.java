package com.xlr8code.server.authentication.service;

import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.utils.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccessTokenServiceTest {

    @Autowired
    private AccessTokenService accessTokenService;

    private String token;

    @BeforeEach
    void setUp() {
        this.token = this.accessTokenService.generate(this.buildTestUser());
    }

    @Test
    void it_should_generate_access_tokens() {
        assertNotNull(this.token);
    }

    @Test
    void it_should_validate_access_tokens() {
        var decodedToken = this.accessTokenService.decode(this.token);

        assertNotNull(decodedToken);
    }

    @Test
    void it_should_return_null_with_invalid_tokens() {
        var decodedToken = this.accessTokenService.decode("invalid");

        assertNull(decodedToken);
    }

    private User buildTestUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .username("test")
                .password("test")
                .roles(Set.of(UserRole.DEFAULT.toRole()))
                .build();

    }

}