package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserPasswordResetCode;
import com.xlr8code.server.authentication.exception.ExpiredPasswordResetCodeException;
import com.xlr8code.server.authentication.exception.InvalidPasswordResetCodeException;
import com.xlr8code.server.authentication.repository.UserPasswordResetCodeRepository;
import com.xlr8code.server.common.utils.DateTimeUtils;
import com.xlr8code.server.common.utils.RandomCode;
import com.xlr8code.server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserPasswordResetCodeService {

    private final UserPasswordResetCodeRepository userPasswordResetCodeRepository;

    @Value("${user.password-reset-code.expiration-time}")
    private long expirationTime;

    @Value("${user.password-reset-code.unit}")
    private ChronoUnit chronoUnit;

    @Value("${user.password-reset-code.length}")
    private int length;

    @Transactional
    public UserPasswordResetCode generate(User user) {

        var userPasswordResetCode = UserPasswordResetCode.builder()
                .code(RandomCode.generate(this.length))
                .user(user)
                .expiresAt(DateTimeUtils.calculateExpiresAt(this.expirationTime, this.chronoUnit))
                .build();

        return this.userPasswordResetCodeRepository.save(userPasswordResetCode);
    }

    @Transactional(readOnly = true)
    public UserPasswordResetCode validate(String code) {
        var userPasswordResetCode = this.userPasswordResetCodeRepository.findByCode(code)
                .orElseThrow(InvalidPasswordResetCodeException::new);

        if (userPasswordResetCode.isExpired()) {
            throw new ExpiredPasswordResetCodeException();
        }

        return userPasswordResetCode;
    }

    @Transactional
    public void removeAllFromUser(User user) {
        this.userPasswordResetCodeRepository.deleteAllByUser(user);
    }

}
