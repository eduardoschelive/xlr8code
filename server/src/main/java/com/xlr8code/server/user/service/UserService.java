package com.xlr8code.server.user.service;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import com.xlr8code.server.user.exception.UserExceptionType;
import com.xlr8code.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMetadataService userMetadataService;

    @Transactional
    public User createUserWithMetadata(User user, UserMetadata userMetadata) throws ApplicationException {
        var userOptional = this.userRepository.findUserByUsernameOrEmailIgnoreCase(user.getUsername(), user.getEmail());

        if (userOptional.isPresent()) {
            throw new ApplicationException(UserExceptionType.USER_ALREADY_EXISTS);
        }

        var savedUser = this.userRepository.save(user);

        var savedUserMetadata = this.userMetadataService.createForUser(savedUser, userMetadata);

        savedUser.setMetadata(savedUserMetadata);

        return savedUser;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        return this.userRepository.findUserByUsernameOrEmailIgnoreCase(login, login);
    }

}
