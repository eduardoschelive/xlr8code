package com.xlr8code.server.user.listener;

import com.xlr8code.server.authentication.service.UserActivationCodeService;
import com.xlr8code.server.common.service.EmailService;
import com.xlr8code.server.user.event.OnCreateUserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserListener implements ApplicationListener<OnCreateUserEvent> {

    private final UserActivationCodeService userActivationCodeService;

    private final EmailService emailService;

    @Override
    public void onApplicationEvent(OnCreateUserEvent event) {
        var user = event.getUser();

        var activationCode = this.userActivationCodeService.generate(user);
        this.emailService.sendActivationEmail(user.getEmail(), activationCode.getCode());
    }
}
