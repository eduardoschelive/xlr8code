package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.UserActivationCode;
import com.xlr8code.server.authentication.exception.ExpiredActivationCodeException;
import com.xlr8code.server.authentication.exception.InvalidActivationCodeException;
import com.xlr8code.server.authentication.repository.UserActivationCodeRepository;
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
public class UserActivationCodeService {

    private final UserActivationCodeRepository userActivationCodeRepository;

    @Value("${user.activation-code.expiration-time}")
    private long expirationTime;

    @Value("${user.activation-code.unit}")
    private ChronoUnit chronoUnit;

    @Value("${user.activation-code.length}")
    private int length;

    @Transactional
    public UserActivationCode generate(User user) {
        var userActivationCode = UserActivationCode.builder()
                .code(RandomCode.generate(this.length))
                .user(user)
                .expiresAt(DateTimeUtils.calculateExpiresAt(this.expirationTime, this.chronoUnit))
                .build();

        return this.userActivationCodeRepository.save(userActivationCode);
    }

    @Transactional(readOnly = true)
    public UserActivationCode validate(String code) {
        var userActivationCode = this.userActivationCodeRepository.findByCode(code)
                .orElseThrow(InvalidActivationCodeException::new);

        if (userActivationCode.isExpired()) {
            throw new ExpiredActivationCodeException();
        }

        return userActivationCode;
    }

    @Transactional
    public void removeAllFromUser(User user) {
        this.userActivationCodeRepository.deleteAllByUser(user);
    }

}
