package com.xlr8code.server.user.service;

import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(User user) {
        return this.userRepository.save(user);
    }

    public User findByLogin(String login) {
        return this.userRepository.findUserByLogin(login);
    }

}
