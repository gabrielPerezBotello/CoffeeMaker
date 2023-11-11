package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * User class for the coffee maker. Tied to the database using Hibernate
 * libraries. See UserRepository and UserService for the other two pieces used
 * for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class User extends DomainObject {

    /** User id */
    @Id
    @GeneratedValue
    private Long     id;

    /** User's username */
    @NotNull
    private String   username;

    /** User's password */
    @NotNull
    private String   password;

    /** User's role */
    @NotNull
    private UserRole role;

    /**
     * Default user constructor
     */
    public User () {
        super();
        this.username = "";
        this.password = "";
        this.role = UserRole.CUSTOMER;
    }

    /**
     * Specific user contructor
     *
     * @param username
     *            the username to set
     * @param password
     *            the password to set
     * @param role
     *            the role
     */
    public User ( @NotNull final String username, @NotNull final String password, @NotNull final UserRole role ) {
        super();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Retrieves the User's username
     *
     * @return username
     */
    public String getUsername () {
        return username;
    }

    /**
     * Sets the User's username
     *
     * @param username
     *            username to set
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

    /**
     * Retrieves the User's password
     *
     * @return password
     */
    public String getPassword () {
        return password;
    }

    /**
     * Sets the User's password
     *
     * @param password
     *            password to set
     */
    public void setPassword ( final String password ) {
        this.password = password;
    }

    /**
     * Retrieves the User's role
     *
     * @return User's role (CUSTOMER, BARISTA, MANAGER)
     */
    public UserRole getRole () {
        return role;
    }

    /**
     * Sets the User's role
     *
     * @param role
     *            Either CUSTOMER, BARISTA, or MANAGER
     */
    public void setRole ( final UserRole role ) {
        this.role = role;
    }

    /**
     * Retrieves the ID of the User
     *
     * @return id the ID
     */
    @Override
    public Long getId () {
        return id;
    }

}
