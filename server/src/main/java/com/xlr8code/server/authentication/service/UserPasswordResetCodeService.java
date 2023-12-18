package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserPasswordResetCode;
import com.xlr8code.server.authentication.exception.AuthenticationExceptionType;
import com.xlr8code.server.authentication.repository.UserPasswordResetCodeRepository;
import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.helper.CodeGenerator;
import com.xlr8code.server.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Getter
@RequiredArgsConstructor
public class UserPasswordResetCodeService {

    private final UserPasswordResetCodeRepository userPasswordResetCodeRepository;
    private final CodeGenerator codeGenerator;

    @Value("${user.password-reset-code.expiration-time}")
    private long expirationTime;

    @Value("${user.password-reset-code.unit}")
    private String unit;

    @Transactional
    public UserPasswordResetCode generate(User user) {

        var userPasswordResetCode = UserPasswordResetCode.builder()
                .code(this.codeGenerator.generatePasswordResetCode())
                .user(user)
                .expiresAt(this.getExpiresAt())
                .build();

        return this.userPasswordResetCodeRepository.save(userPasswordResetCode);
    }

    @Transactional(readOnly = true)
    public UserPasswordResetCode validate(String code) {
        var userPasswordResetCode = this.userPasswordResetCodeRepository.findByCode(code)
                .orElseThrow(() -> new ApplicationException(AuthenticationExceptionType.INVALID_PASSWORD_RESET_CODE));

        if (userPasswordResetCode.isExpired()) {
            throw new ApplicationException(AuthenticationExceptionType.EXPIRED_PASSWORD_RESET_CODE);
        }

        return userPasswordResetCode;
    }

    @Transactional
    public void removeAllFromUser(User user) {
        this.userPasswordResetCodeRepository.deleteAllByUser(user);
    }

    private Date getExpiresAt() {
        return Date.from(Instant.now().plus(this.getExpirationTime(), this.getChronoUnit()));
    }

    private ChronoUnit getChronoUnit() {
        var unitName = this.getUnit().toUpperCase();
        return ChronoUnit.valueOf(unitName);
    }

}
