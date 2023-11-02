package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.transaction.Transactional;

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

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class UserTest {

    @Autowired
    private UserService service;

    private User        tav;

    /**
     * Sets up testing environment.
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
        tav = new User( "Tav", "password123", UserRole.CUSTOMER );
    }

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

    @Test
    @Transactional
    void testGetUsername () {
        Assertions.assertEquals( "Tav", tav.getUsername() );
    }

    @Test
    @Transactional
    void testSetUsername () {
        Assertions.assertDoesNotThrow( () -> tav.setUsername( "DiffUsername" ) );
        Assertions.assertEquals( "DiffUsername", tav.getUsername() );
    }

    @Test
    @Transactional
    void testGetPassword () {
        Assertions.assertEquals( "password123", tav.getPassword() );
    }

    @Test
    @Transactional
    void testSetPassword () {
        Assertions.assertDoesNotThrow( () -> tav.setPassword( "newpassword123" ) );
        Assertions.assertEquals( "newpassword123", tav.getPassword() );
    }

    @Test
    @Transactional
    void testGetRole () {
        Assertions.assertEquals( UserRole.CUSTOMER, tav.getRole() );
    }

    @Test
    @Transactional
    void testSetRole () {
        Assertions.assertDoesNotThrow( () -> tav.setRole( UserRole.BARISTA ) );
        Assertions.assertEquals( UserRole.BARISTA, tav.getRole() );
    }

}
