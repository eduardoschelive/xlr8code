package com.xlr8code.server.user.service;

import com.xlr8code.server.user.repository.RoleRepository;
import com.xlr8code.server.user.utils.AvailableRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public void createDefaultRoles() {
        var roles = Arrays.stream(AvailableRoles.values()).map(AvailableRoles::toRole).toList();
        this.roleRepository.saveAll(roles);
    }

}
