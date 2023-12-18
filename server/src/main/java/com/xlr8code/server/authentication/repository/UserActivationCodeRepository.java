package com.xlr8code.server.authentication.repository;

import com.xlr8code.server.authentication.entity.UserActivationCode;
import com.xlr8code.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserActivationCodeRepository extends JpaRepository<UserActivationCode, Long> {

    Optional<UserActivationCode> findByCode(String code);

    void deleteAllByUser(User user);
}
