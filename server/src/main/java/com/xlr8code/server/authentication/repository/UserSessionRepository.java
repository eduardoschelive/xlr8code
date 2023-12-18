package com.xlr8code.server.authentication.repository;

import com.xlr8code.server.authentication.entity.UserSession;
import com.xlr8code.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findBySessionToken(UUID sessionToken);

    void deleteAllByUser(User user);

}
