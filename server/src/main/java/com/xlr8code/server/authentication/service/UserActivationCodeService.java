package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserActivationCode;
import com.xlr8code.server.authentication.exception.ExpiredActivationCodeException;
import com.xlr8code.server.authentication.exception.InvalidActivationCodeException;
import com.xlr8code.server.authentication.repository.UserActivationCodeRepository;
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

    private final UserActivationCodeRepository userActivationCodeRepository;

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
     * @return the generated {@link UserActivationCode}
     */
    @Transactional
    public UserActivationCode generate(User user) {
        var userActivationCode = UserActivationCode.builder()
                .code(RandomUtils.generate(this.length))
                .user(user)
                .expiresAt(DateTimeUtils.calculateExpiresAt(this.expirationTime, this.chronoUnit))
                .build();

        return this.userActivationCodeRepository.save(userActivationCode);
    }

    /**
     * @param code the code to decode
     * @return the validated {@link UserActivationCode}
     * @throws InvalidActivationCodeException if the code is invalid
     * @throws ExpiredActivationCodeException if the code is expired
     */
    @Transactional(readOnly = true)
    public UserActivationCode validate(String code) {
        var userActivationCode = this.userActivationCodeRepository.findByCode(code)
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
        this.userActivationCodeRepository.deleteAllByUser(user);
    }

}
