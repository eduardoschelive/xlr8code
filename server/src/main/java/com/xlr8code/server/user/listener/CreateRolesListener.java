package com.xlr8code.server.user.listener;

import com.xlr8code.server.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CreateRolesListener implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleService roleService;


    @Override
    public void onApplicationEvent(@Nullable ContextRefreshedEvent event) {
        this.roleService.createRoles();
    }

}
