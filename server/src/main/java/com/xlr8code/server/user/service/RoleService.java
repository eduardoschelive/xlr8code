package com.xlr8code.server.user.service;

import com.xlr8code.server.user.repository.RoleRepository;
import com.xlr8code.server.user.utils.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public void createRoles() {
        var roles = Arrays.stream(UserRole.values()).map(UserRole::toRole).toList();
        this.roleRepository.saveAll(roles);
    }

}
