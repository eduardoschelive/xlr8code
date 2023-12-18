package com.xlr8code.server.authentication.listener;

import com.xlr8code.server.authentication.events.OnSignUpCompleteEvent;
import com.xlr8code.server.authentication.service.VerificationTokenService;
import com.xlr8code.server.authentication.utils.Endpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpListener implements ApplicationListener<OnSignUpCompleteEvent> {


    private final VerificationTokenService verificationTokenService;

    @Override
    public void onApplicationEvent(OnSignUpCompleteEvent event) {
        var user = event.getUser();
        var appUrl = event.getAppUrl();

        var verificationToken = this.verificationTokenService.createVerificationToken(user);
        var url = appUrl + Endpoint.VERIFY_TOKEN + "?token=" + verificationToken.getToken();
        System.out.println(url);

    }
}
