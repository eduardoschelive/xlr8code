package com.xlr8code.server.authentication.repository;

import com.xlr8code.server.authentication.entity.UserCode;
import com.xlr8code.server.authentication.entity.UserCodeType;
import com.xlr8code.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserCodeRepository extends JpaRepository<UserCode, Long> {

    Optional<UserCode> findByCodeAndCodeType(String code, UserCodeType type);

    List<UserCode> findAllByUserAndCodeType(User user, UserCodeType type);

    void deleteAllByUserAndCodeType(User user, UserCodeType type);

    void deleteAllByExpiresAtBefore(Instant expiresAt);

}
