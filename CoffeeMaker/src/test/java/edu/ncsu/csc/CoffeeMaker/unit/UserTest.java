package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.UserRole;

class UserTest {

    private User tav;

    /**
     * Sets up testing environment.
     */
    @BeforeEach
    public void setup () {
        tav = new User( "Tav", "password123", UserRole.CUSTOMER );
    }

    @Test
    void testUser () {

    }

    @Test
    void testGetUsername () {
        Assertions.assertEquals( "Tav", tav.getUsername() );
    }

    @Test
    void testSetUsername () {
        Assertions.assertDoesNotThrow( () -> tav.setUsername( "DiffUsername" ) );
        Assertions.assertEquals( "DiffUsername", tav.getUsername() );
    }

    @Test
    void testGetPassword () {
        Assertions.assertEquals( "password123", tav.getPassword() );
    }

    @Test
    void testSetPassword () {
        Assertions.assertDoesNotThrow( () -> tav.setPassword( "newpassword123" ) );
        Assertions.assertEquals( "newpassword123", tav.getPassword() );
    }

    @Test
    void testGetRole () {
        Assertions.assertEquals( UserRole.CUSTOMER, tav.getRole() );
    }

    @Test
    void testSetRole () {
        Assertions.assertDoesNotThrow( () -> tav.setRole( UserRole.BARISTA ) );
        Assertions.assertEquals( UserRole.BARISTA, tav.getRole() );
    }

}
