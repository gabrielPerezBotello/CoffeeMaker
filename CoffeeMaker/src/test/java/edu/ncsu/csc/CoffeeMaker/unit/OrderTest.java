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
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.OrderStatus;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class OrderTest {

    /**
     * RecipeService object to be used in testing.
     */
    @Autowired
    private OrderService service;

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

        final Order o1 = new Order( "Sharon", 1, (long) 100 );
        o1.setPayment( 10 );
        o1.setReview( "good" );

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

        o1.setRecipe( r1 );
        // Save order

        service.save( o1 );
        final Order r2 = new Order( "John", 2, (long) 120 );
        o1.setPayment( 15 );
        o1.setReview( "fine" );
        r2.setRecipe( r1 );
        service.save( r2 );

        final List<Order> orders = service.findAll();
        Assertions.assertEquals( 2, orders.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( o1, orders.get( 0 ), "The retrieved recipe should match the created one" );

        assertEquals( r1.getName(), o1.getRecipe().getName() );
        o1.advanceOrder();
        assertTrue( o1.getStatus().equals( OrderStatus.FULFILLED ) );
        o1.advanceOrder();
        o1.toString();
        final Order o3 = new Order( "Sharon", 1, (long) 100 );

        assertTrue( o1.equals( o3 ) );

        final Order o4 = new Order( null, 1, (long) 100 );

        assertFalse( o4.equals( o3 ) );

    }

}
