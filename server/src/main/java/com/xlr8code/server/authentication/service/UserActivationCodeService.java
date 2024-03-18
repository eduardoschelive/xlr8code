package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserCode;
import com.xlr8code.server.authentication.entity.UserCodeType;
import com.xlr8code.server.authentication.exception.AccountAlreadyActivatedException;
import com.xlr8code.server.authentication.exception.ExpiredActivationCodeException;
import com.xlr8code.server.authentication.exception.InvalidActivationCodeException;
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
public class UserActivationCodeService {

    private final UserCodeRepository userCodeRepository;

    @Value("${user.activation-code.expiration-time}")
    private long expirationTime;

    @Value("${user.activation-code.unit}")
    private ChronoUnit chronoUnit;

    @Value("${user.activation-code.length}")
    private int length;

    /**
     * <p>
     * Generates a new activation code for the given user. The expiration date is configured in the application
     * properties.
     * </p>
     *
     * @param user the user to generate the code for
     * @return the generated {@link UserCode}
     */
    @Transactional
    public UserCode generate(User user) {
        if (user.isActive()) {
            throw new AccountAlreadyActivatedException();
        }

        var userActivationCode = UserCode.builder()
                .code(RandomUtils.generate(this.length))
                .user(user)
                .codeType(UserCodeType.ACTIVATION)
                .expiresAt(DateTimeUtils.calculateExpiresAt(this.expirationTime, this.chronoUnit))
                .build();

        return this.userCodeRepository.save(userActivationCode);
    }

    /**
     * @param code the code to decode
     * @return the validated {@link UserCode}
     * @throws InvalidActivationCodeException if the code is invalid
     * @throws ExpiredActivationCodeException if the code is expired
     */
    @Transactional(readOnly = true)
    public UserCode validate(String code) {
        var userActivationCode = this.userCodeRepository.findByCodeAndCodeType(code, UserCodeType.ACTIVATION)
                .orElseThrow(InvalidActivationCodeException::new);

        if (userActivationCode.isExpired()) {
            throw new ExpiredActivationCodeException();
        }

        return userActivationCode;
    }

    /**
     * @param user the user to remove the codes from
     */
    @Transactional
    public void removeAllFromUser(User user) {
        this.userCodeRepository.deleteAllByUserAndCodeType(user, UserCodeType.ACTIVATION);
    }

}
