package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserPasswordResetCode;
import com.xlr8code.server.authentication.exception.ExpiredPasswordResetCodeException;
import com.xlr8code.server.authentication.exception.InvalidPasswordResetCodeException;
import com.xlr8code.server.authentication.repository.UserPasswordResetCodeRepository;
import com.xlr8code.server.common.helper.CodeGenerator;
import com.xlr8code.server.common.utils.TimeUtils;
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
    private ChronoUnit chronoUnit;

    @Transactional
    public UserPasswordResetCode generate(User user) {

        var userPasswordResetCode = UserPasswordResetCode.builder()
                .code(this.codeGenerator.generatePasswordResetCode())
                .user(user)
                .expiresAt(TimeUtils.calculateExpiresAt(this.getExpirationTime(), this.getChronoUnit()))
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
