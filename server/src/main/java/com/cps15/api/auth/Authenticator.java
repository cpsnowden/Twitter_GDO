package com.cps15.api.auth;

import com.cps15.api.data.User;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mongodb.annotations.Immutable;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentials;
import org.eclipse.jetty.server.Authentication;

import java.util.Map;
import java.util.Set;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public class Authenticator implements io.dropwizard.auth.Authenticator<BasicCredentials, User> {

    private static final Map<String, Set<String>> VALID_USERS = ImmutableMap.of(
            "cps15_user", ImmutableSet.of("USER"),
            "cps15_admin", ImmutableSet.of("ADMIN"),
            "app", ImmutableSet.of("APP")
    );


    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
//        System.out.println(credentials.getUsername());
        if (VALID_USERS.containsKey(credentials.getUsername()) && "secret".equals(credentials.getPassword())) {
            return Optional.of(new User(credentials.getUsername(), VALID_USERS.get(credentials.getUsername())));
        }
        return Optional.absent();
    }

}
