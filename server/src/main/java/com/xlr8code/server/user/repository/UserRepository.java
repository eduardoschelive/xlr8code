package com.xlr8code.server.domain.user.repository;

import com.xlr8code.server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

        boolean existsByEmail(String email);

        boolean existsByUsername(String username);

        User findByEmail(String email);

        User findByUsername(String username);

}
