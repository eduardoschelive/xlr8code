package com.xlr8code.server.authentication.repository;

import com.xlr8code.server.authentication.entity.UserPasswordResetCode;
import com.xlr8code.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPasswordResetCodeRepository extends JpaRepository<UserPasswordResetCode, Long> {

    Optional<UserPasswordResetCode> findByCode(String code);

    List<UserPasswordResetCode> findAllByUser(User user);

    void deleteAllByUser(User user);

}
