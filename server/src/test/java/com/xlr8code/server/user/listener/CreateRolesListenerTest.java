package com.xlr8code.server.user.listener;

import com.xlr8code.server.user.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.ContextRefreshedEvent;

import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateRolesListenerTest {

    @Mock
    private RoleService roleService;

    private CreateRolesListener createRolesListener;

    @BeforeEach
    void setUp() {
        this.createRolesListener = new CreateRolesListener(roleService);
    }

    @Test
    void it_should_call_create_roles_once() {
        var applicationEvent = Mockito.mock(ContextRefreshedEvent.class);
        this.createRolesListener.onApplicationEvent(applicationEvent);

        Mockito.verify(roleService, times(1)).createRoles();
    }

}