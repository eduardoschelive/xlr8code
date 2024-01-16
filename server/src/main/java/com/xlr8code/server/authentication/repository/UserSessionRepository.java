package com.xlr8code.server.authentication.repository;

import com.xlr8code.server.authentication.entity.UserSession;
import com.xlr8code.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findBySessionToken(String sessionToken);

    void deleteAllByUser(User user);

    List<UserSession> findAllByUser(User user);

}
