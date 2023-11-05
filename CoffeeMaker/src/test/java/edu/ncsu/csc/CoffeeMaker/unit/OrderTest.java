package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.OrderStatus;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.CustomerOrderService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class OrderTest {

    /**
     * RecipeService object to be used in testing.
     */
    @Autowired
    private CustomerOrderService service;

    /**
     * Sets up testing environment.
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Tests adding a recipe to system.
     */
    @Test
    @Transactional
    public void testAddOrder () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        // Create Ingredients
        final Ingredient coffee = new Ingredient( "Coffee", 1 );
        final Ingredient milk = new Ingredient( "Milk", 0 );
        final Ingredient sugar = new Ingredient( "Sugar", 0 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 0 );
        // Add Ingredients to Recipe
        r1.addIngredient( coffee );
        r1.addIngredient( milk );
        r1.addIngredient( sugar );
        r1.addIngredient( chocolate );

        final CustomerOrder o1 = new CustomerOrder( "Sharon", 1, r1 );
        o1.setPayment( 10 );
        o1.setReview( "good" );

        // Save order

        service.save( o1 );

        final CustomerOrder o2 = new CustomerOrder( "John", 2, r1 );
        o2.setPayment( 15 );
        o2.setReview( "fine" );

        // Save order

        service.save( o2 );

        final List<CustomerOrder> orders = service.findAll();
        Assertions.assertEquals( 2, orders.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( o1, orders.get( 0 ), "The retrieved recipe should match the created one" );

        assertEquals( r1.getName(), o1.getRecipe().getName() );
        o1.advanceStatus();
        assertTrue( o1.getStatus().equals( OrderStatus.FULFILLED ) );
        o1.advanceStatus();
        o1.toString();
        final CustomerOrder o3 = new CustomerOrder( "Sharon", 1, r1 );

        assertTrue( o1.equals( o3 ) );

        final CustomerOrder o4 = new CustomerOrder( null, 1, r1 );

        assertFalse( o4.equals( o3 ) );

        assertTrue( o1.getCustomerName().equals( "Sharon" ) );
        assertTrue( o1.getOrderID().equals( 1 ) );
        assertTrue( o1.getReview().equals( "good" ) );
        assertTrue( o1.getPlacementTime() > 0 );
        assertTrue( o1.getPayment() == 10 );

        assertTrue( service.findByCustomerName( "Sharon" ).equals( o1 ) );
        assertTrue( service.findByOrderID( 1 ).equals( o1 ) );

    }
}
