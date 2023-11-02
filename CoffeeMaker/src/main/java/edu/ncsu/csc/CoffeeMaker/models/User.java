package edu.ncsu.csc.CoffeeMaker.models;

import javax.validation.constraints.NotNull;

public class User {

    @NotNull
    private String   username;

    @NotNull
    private String   password;

    @NotNull
    private UserRole role;

    public User ( @NotNull final String username, @NotNull final String password, @NotNull final UserRole role ) {
        super();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername () {
        return username;
    }

    public void setUsername ( final String username ) {
        this.username = username;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword ( final String password ) {
        this.password = password;
    }

    public UserRole getRole () {
        return role;
    }

    public void setRole ( final UserRole role ) {
        this.role = role;
    }

}
