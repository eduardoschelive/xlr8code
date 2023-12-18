package com.xlr8code.server.user.service;

import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import com.xlr8code.server.user.exception.UserAlreadyExistsException;
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
    public User createUserWithMetadata(User user, UserMetadata userMetadata) {
        var userOptional = this.userRepository.findUserByUsernameOrEmailIgnoreCase(user.getUsername(), user.getEmail());

        if (userOptional.isPresent()) {
            throw new UserAlreadyExistsException();
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

    @Transactional
    public void updatePassword(User user, String password) {
        user.setPasswordHash(password);
        this.userRepository.save(user);
    }

    @Transactional
    public void activate(User user) {
        user.activate();
        this.userRepository.save(user);
    }

}
