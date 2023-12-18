package com.xlr8code.server.authentication.service;

import com.xlr8code.server.authentication.entity.VerificationToken;
import com.xlr8code.server.authentication.repository.VerificationTokenRepository;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserService userService;

    public VerificationToken createVerificationToken(User user) {
        VerificationToken verificationToken = VerificationToken.generate(user);
        return this.verificationTokenRepository.save(verificationToken);
    }

    public VerificationToken getVerificationToken(String token) {
        return this.verificationTokenRepository.findByToken(token);
    }

    public void verify(String token) {
        var verificationToken = this.getVerificationToken(token);

        // TODO: Create a custom exception for this
        if (verificationToken == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        if (verificationToken.isExpired()) {
            throw new IllegalArgumentException("Token expired");
        }

        var user = verificationToken.getUser();
        user.activate();

        this.userService.update(user);
        this.verificationTokenRepository.delete(verificationToken);

    }

}
