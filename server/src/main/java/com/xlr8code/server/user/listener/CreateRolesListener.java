package com.xlr8code.server.user.listener;

import com.xlr8code.server.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class CreateRolesListener implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleService roleService;


    @Override
    public void onApplicationEvent(@Nullable ContextRefreshedEvent event) {
        this.roleService.createRoles();
    }

}
