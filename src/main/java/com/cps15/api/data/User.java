package com.cps15.api.data;

import java.security.Principal;
import java.util.Set;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public class User implements Principal {

    private String name;
    private final Set<String> roles;

    @Override
    public String getName() {
        return null;
    }

    public User(String name) {
        this.name = name;
        this.roles = null;
    }

    public User(String name, Set<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    public Set<String> getRoles() {
        return roles;
    }

}
