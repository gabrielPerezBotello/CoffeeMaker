package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.UserRole;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Tests the User class
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class UserTest {

    /**
     * UserService object to be used in testing
     */
    @Autowired
    private UserService service;

    /**
     * User object to be used in testing
     */
    private User        tav;

    /**
     * Sets up testing environment.
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
        tav = new User( "Tav", "password123", UserRole.CUSTOMER );
    }

    /**
     * Tests creation of User in database
     */
    @Test
    @Transactional
    void testUser () {
        final User customer = new User( "customer", "password123", UserRole.CUSTOMER );
        final User barista = new User( "barista", "password123", UserRole.BARISTA );

        service.save( customer );
        service.save( barista );

        final List<User> users = service.findAll();
        Assertions.assertEquals( 2, users.size(), "Creating two users should result in two users in the database" );

        Assertions.assertEquals( customer, users.get( 0 ), "First user should be the customer" );
        Assertions.assertEquals( barista, users.get( 1 ), "Second user should be the barista" );
    }

    /**
     * Tests creation of User in database
     */
    @Test
    @Transactional
    void testInvalidUser () {
        final User customer0 = new User( null, "pass", UserRole.CUSTOMER );
        final User customer1 = new User( "user", null, UserRole.CUSTOMER );
        final User customer2 = new User( "user", "pass", null );

        Assertions.assertThrows( ConstraintViolationException.class, () -> service.save( customer0 ) );
        Assertions.assertThrows( ConstraintViolationException.class, () -> service.save( customer1 ) );
        Assertions.assertThrows( ConstraintViolationException.class, () -> service.save( customer2 ) );
    }

    /**
     * Tests getUsername()
     */
    @Test
    @Transactional
    void testGetUsername () {
        Assertions.assertEquals( "Tav", tav.getUsername() );

        Assertions.assertNotEquals( "", tav.getUsername() );
        Assertions.assertNotEquals( null, tav.getUsername() );
    }

    /**
     * Tests setUsername()
     */
    @Test
    @Transactional
    void testSetUsername () {
        Assertions.assertDoesNotThrow( () -> tav.setUsername( "DiffUsername" ) );
        Assertions.assertEquals( "DiffUsername", tav.getUsername() );

        Assertions.assertNotEquals( "", tav.getUsername() );
        Assertions.assertNotEquals( null, tav.getUsername() );
    }

    /**
     * Tests getPassword
     */
    @Test
    @Transactional
    void testGetPassword () {
        Assertions.assertEquals( "password123", tav.getPassword() );

        Assertions.assertNotEquals( "", tav.getPassword() );
        Assertions.assertNotEquals( null, tav.getPassword() );
    }

    /**
     * Tests setPassword
     */
    @Test
    @Transactional
    void testSetPassword () {
        Assertions.assertDoesNotThrow( () -> tav.setPassword( "newpassword123" ) );
        Assertions.assertEquals( "newpassword123", tav.getPassword() );

        Assertions.assertNotEquals( "", tav.getPassword() );
        Assertions.assertNotEquals( null, tav.getPassword() );
    }

    /**
     * Tests getRole()
     */
    @Test
    @Transactional
    void testGetRole () {
        Assertions.assertEquals( UserRole.CUSTOMER, tav.getRole() );

        Assertions.assertNotEquals( UserRole.BARISTA, tav.getRole() );
        Assertions.assertNotEquals( UserRole.MANAGER, tav.getRole() );
    }

    /**
     * Tests setRole()
     */
    @Test
    @Transactional
    void testSetRole () {
        Assertions.assertDoesNotThrow( () -> tav.setRole( UserRole.BARISTA ) );
        Assertions.assertEquals( UserRole.BARISTA, tav.getRole() );

        Assertions.assertNotEquals( UserRole.CUSTOMER, tav.getRole() );
        Assertions.assertNotEquals( UserRole.MANAGER, tav.getRole() );
    }

}
