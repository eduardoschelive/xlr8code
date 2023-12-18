package com.xlr8code.server.common.listener;

import com.xlr8code.server.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleService roleService;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        this.roleService.createDefaultRoles();
    }
}
