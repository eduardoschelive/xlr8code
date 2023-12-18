package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserActivationCode;
import com.xlr8code.server.authentication.exception.AuthenticationExceptionType;
import com.xlr8code.server.authentication.repository.UserActivationCodeRepository;
import com.xlr8code.server.common.exception.ApplicationException;
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
public class UserActivationCodeService {

    private final UserActivationCodeRepository userActivationCodeRepository;

    @Value("${user.activation-code.expiration-time}")
    private long expirationTime;

    @Value("${user.activation-code.unit}")
    private String unit;

    @Transactional
    public UserActivationCode generate(User user) {
        var userActivationCode = new UserActivationCode(user, this.getExpiresAt());

        return this.userActivationCodeRepository.save(userActivationCode);
    }

    @Transactional(readOnly = true)
    public UserActivationCode validate(String code) {
        var userActivationCode = this.userActivationCodeRepository.findByCode(code)
                .orElseThrow(() -> new ApplicationException(AuthenticationExceptionType.INVALID_ACTIVATION_CODE));

        if (userActivationCode.isExpired()) {
            throw new ApplicationException(AuthenticationExceptionType.EXPIRED_ACTIVATION_CODE);
        }

        return userActivationCode;
    }

    @Transactional
    public void removeAllFromUser(User user) {
        this.userActivationCodeRepository.deleteAllByUser(user);
    }

    private Date getExpiresAt() {
        return Date.from(Instant.now().plus(this.getExpirationTime(), this.getChronoUnit()));
    }

    private ChronoUnit getChronoUnit() {
        var unitName = this.getUnit().toUpperCase();
        return ChronoUnit.valueOf(unitName);
    }

}
