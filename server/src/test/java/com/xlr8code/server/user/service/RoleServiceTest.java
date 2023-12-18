package com.xlr8code.server.user.service;

import com.xlr8code.server.user.repository.RoleRepository;
import com.xlr8code.server.user.utils.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RoleServiceTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    static void setUp(@Autowired RoleService roleService) {
        roleService.createRoles();
    }

    @ParameterizedTest
    @EnumSource(UserRole.class)
    void it_should_create_roles(UserRole userRole) {
        var role = roleRepository.findById(userRole.getId());
        assertTrue(role.isPresent());
    }

    @ParameterizedTest
    @EnumSource(UserRole.class)
    void it_should_have_roles_values_matching(UserRole userRole) {
        var role = roleRepository.findById(userRole.getId());
        var roleObject = role.orElseThrow();
        var roleValue = roleObject.getUserRole().getValue();
        assertEquals(userRole.getValue(), roleValue);
    }

    @Nested
    class DuplicationTests {

        @BeforeAll
        static void setUp(@Autowired RoleService roleService) {
            roleService.createRoles(); // Create roles again
        }

        @ParameterizedTest
        @EnumSource(UserRole.class)
        void it_should_not_have_duplicate_roles(UserRole userRole) {
            var amountExpected = 1;
            var example = Example.of(userRole.toRole());

            var roles = roleRepository.findAll(example);

            assertEquals(amountExpected, roles.size());
        }


    }

}

