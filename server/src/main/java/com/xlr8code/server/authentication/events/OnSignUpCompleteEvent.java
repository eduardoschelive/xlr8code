package com.xlr8code.server.authentication.events;

import com.xlr8code.server.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnSignUpCompleteEvent extends ApplicationEvent {

    private final User user;
    private final String appUrl;

    public OnSignUpCompleteEvent(User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }

}
