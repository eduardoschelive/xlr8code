package com.xlr8code.server.common.listener;

import com.xlr8code.server.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultUserRolesStartup {

    private final RoleService roleService;

    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshed() {
        this.roleService.createDefaultRoles();
    }
}
