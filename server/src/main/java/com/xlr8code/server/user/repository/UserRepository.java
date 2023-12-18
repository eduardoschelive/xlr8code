package com.xlr8code.server.user.repository;

import com.xlr8code.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.email = :login OR u.username = :login")
    User findUserByLogin(@Param("login") String login);

    Optional<User> findUserByUsernameOrEmailIgnoreCase(String username, String email);

}
