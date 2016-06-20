package com.cps15.api.auth;

import com.cps15.api.data.User;
import io.dropwizard.auth.Auth;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public class Authorizer implements io.dropwizard.auth.Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        return user.getRoles() != null && user.getRoles().contains(role);
    }
}
