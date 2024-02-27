package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserCode;
import com.xlr8code.server.authentication.entity.UserCodeType;
import com.xlr8code.server.authentication.exception.ExpiredPasswordResetCodeException;
import com.xlr8code.server.authentication.exception.InvalidPasswordResetCodeException;
import com.xlr8code.server.authentication.repository.UserCodeRepository;
import com.xlr8code.server.common.utils.DateTimeUtils;
import com.xlr8code.server.common.utils.RandomUtils;
import com.xlr8code.server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserPasswordResetCodeService {

    private final UserCodeRepository userCodeRepository;

    @Value("${user.password-reset-code.expiration-time}")
    private long expirationTime;

    @Value("${user.password-reset-code.unit}")
    private ChronoUnit chronoUnit;

    @Value("${user.password-reset-code.length}")
    private int length;

    /**
     * @param user the user to generate the code for
     * @return the generated {@link UserCode}
     */
    @Transactional
    public UserCode generate(User user) {

        var userPasswordResetCode = UserCode.builder()
                .code(RandomUtils.generate(this.length))
                .user(user)
                .codeType(UserCodeType.PASSWORD_RESET)
                .expiresAt(DateTimeUtils.calculateExpiresAt(this.expirationTime, this.chronoUnit))
                .build();

        return this.userCodeRepository.save(userPasswordResetCode);
    }

    /**
     * @param code the code to decode
     * @return the validated {@link UserCode}
     * @throws InvalidPasswordResetCodeException if the code is invalid
     * @throws ExpiredPasswordResetCodeException if the code is expired
     */
    @Transactional(readOnly = true)
    public UserCode validate(String code) {
        var userPasswordResetCode = this.userCodeRepository.findByCodeAndCodeType(code, UserCodeType.PASSWORD_RESET)
                .orElseThrow(InvalidPasswordResetCodeException::new);

        if (userPasswordResetCode.isExpired()) {
            throw new ExpiredPasswordResetCodeException();
        }

        return userPasswordResetCode;
    }

    /**
     * @param user the user to remove the codes from
     */
    @Transactional
    public void removeAllFromUser(User user) {
        this.userCodeRepository.deleteAllByUserAndCodeType(user, UserCodeType.PASSWORD_RESET);
    }

}
