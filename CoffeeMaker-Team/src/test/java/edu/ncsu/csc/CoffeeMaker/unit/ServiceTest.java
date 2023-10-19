package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Thes testing class for Service.java
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class ServiceTest {

    /**
     * The RecipeService object used for testing
     */
    @Autowired
    private RecipeService service;

    /**
     * Responsible for setting up test environment.
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Tests findById() method.
     */
    @Test
    @Transactional
    public void testFindById () {
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        // r1.setCoffee( 1 );
        // r1.setMilk( 0 );
        // r1.setSugar( 0 );
        // r1.setChocolate( 0 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        // r2.setCoffee( 1 );
        // r2.setMilk( 1 );
        // r2.setSugar( 1 );
        // r2.setChocolate( 1 );
        service.save( r2 );
        final long r2Id = service.findByName( "Mocha" ).getId();
        assertEquals( r2, service.findById( r2Id ) );

    }

    /**
     * Tests findById method with null parameter
     */
    @Test
    @Transactional
    public void testFindByIdNull () {

        assertNull( service.findById( null ) );

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        // r1.setCoffee( 1 );
        // r1.setMilk( 0 );
        // r1.setSugar( 0 );
        // r1.setChocolate( 0 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        // r2.setCoffee( 1 );
        // r2.setMilk( 1 );
        // r2.setSugar( 1 );
        // r2.setChocolate( 1 );
        service.save( r2 );
        final long r2Id = service.findByName( "Mocha" ).getId();
        assertEquals( r2, service.findById( r2Id ) );

        assertNull( service.findById( (long) 50 ) );
    }

    /**
     * Tests existsById() method.
     */
    @Test
    @Transactional
    public void testExistsById () {
        assertNull( service.findByName( null ) );

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        // r1.setCoffee( 1 );
        // r1.setMilk( 0 );
        // r1.setSugar( 0 );
        // r1.setChocolate( 0 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        // r2.setCoffee( 1 );
        // r2.setMilk( 1 );
        // r2.setSugar( 1 );
        // r2.setChocolate( 1 );
        service.save( r2 );
        final long r2Id = service.findByName( "Mocha" ).getId();
        assertTrue( service.existsById( r2Id ) );

    }

}
