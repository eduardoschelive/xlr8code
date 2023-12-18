package com.xlr8code.server.user.event;

import com.xlr8code.server.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnCreateUserEvent extends ApplicationEvent {

    private final User user;

    public OnCreateUserEvent(User user) {
        super(user);
        this.user = user;
    }

}
