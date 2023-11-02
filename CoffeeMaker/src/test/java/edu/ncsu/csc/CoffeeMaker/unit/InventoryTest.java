package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.ArrayList;
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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * Tests Inventory class.
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    /**
     * InventoryService object to be used while testing.
     */
    @Autowired
    private InventoryService inventoryService;

    /**
     * Sets up testing environment.
     */
    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();
        final Ingredient chocolate = new Ingredient( "Chocolate", 500 );
        final Ingredient coffee = new Ingredient( "Coffee", 500 );
        final Ingredient milk = new Ingredient( "Milk", 500 );
        final Ingredient sugar = new Ingredient( "Sugar", 500 );

        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( chocolate );
        ingredients.add( coffee );
        ingredients.add( sugar );
        ingredients.add( milk );

        ivt.addIngredients( ingredients );

        inventoryService.save( ivt );
    }

    /**
     * Tests consuming ingredients from inventory.
     */
    @Test
    @Transactional
    public void testConsumeInventory1 () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        // Create Ingredients
        final Ingredient chocolate = new Ingredient( "Chocolate", 10 );
        final Ingredient milk = new Ingredient( "Milk", 20 );
        final Ingredient sugar = new Ingredient( "Sugar", 5 );
        final Ingredient coffee = new Ingredient( "Coffee", 1 );
        // Add ingredients to recipe
        recipe.addIngredient( chocolate );
        recipe.addIngredient( milk );
        recipe.addIngredient( sugar );
        recipe.addIngredient( coffee );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 490, (int) i.getIngredient( "Chocolate" ).getAmount() );
        Assertions.assertEquals( 480, (int) i.getIngredient( "Milk" ).getAmount() );
        Assertions.assertEquals( 495, (int) i.getIngredient( "Sugar" ).getAmount() );
        Assertions.assertEquals( 499, (int) i.getIngredient( "Coffee" ).getAmount() );
    }

    /**
     * Tests consuming ingredients from inventory.
     */
    @Test
    @Transactional
    public void testConsumeInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Coffee" );
        // Create Ingredients
        final Ingredient chocolate = new Ingredient( "Chocolate", 10 );
        final Ingredient milk = new Ingredient( "Milk", 20 );
        final Ingredient sugar = new Ingredient( "Sugar", 5 );
        final Ingredient coffee = new Ingredient( "Coffee", 1 );
        // Add ingredients to recipe
        recipe.addIngredient( chocolate );
        recipe.addIngredient( milk );
        recipe.addIngredient( sugar );
        recipe.addIngredient( coffee );
        recipe.setPrice( 5 );

        // test with enough of each ingredient for inventory
        final Ingredient chocolate1 = new Ingredient( "Chocolate", 500 );
        final Ingredient coffee1 = new Ingredient( "Coffee", 500 );
        final Ingredient milk1 = new Ingredient( "Milk", 500 );
        final Ingredient sugar1 = new Ingredient( "Sugar", 500 );
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( chocolate1 );
        ingredients.add( coffee1 );
        ingredients.add( milk1 );
        ingredients.add( sugar1 );
        ivt.setIngredients( ingredients );

        Assertions.assertTrue( ivt.useIngredients( recipe ) );
        Assertions.assertEquals( 490, (int) ivt.getIngredient( "Chocolate" ).getAmount() );
        Assertions.assertEquals( 480, (int) ivt.getIngredient( "Milk" ).getAmount() );
        Assertions.assertEquals( 495, (int) ivt.getIngredient( "Sugar" ).getAmount() );
        Assertions.assertEquals( 499, (int) ivt.getIngredient( "Coffee" ).getAmount() );

        // test with not enough of chocolate ingredient for inventory
        final Ingredient chocolate2 = new Ingredient( "Chocolate", 0 );
        final Ingredient coffee2 = new Ingredient( "Coffee", 500 );
        final Ingredient milk2 = new Ingredient( "Milk", 500 );
        final Ingredient sugar2 = new Ingredient( "Sugar", 500 );
        final List<Ingredient> ingredients2 = new ArrayList<Ingredient>();
        ingredients2.add( chocolate2 );
        ingredients2.add( coffee2 );
        ingredients2.add( milk2 );
        ingredients2.add( sugar2 );
        ivt.setIngredients( ingredients2 );

        Assertions.assertFalse( ivt.useIngredients( recipe ) );
        Assertions.assertEquals( 0, (int) ivt.getIngredient( "Chocolate" ).getAmount() );
        Assertions.assertEquals( 500, (int) ivt.getIngredient( "Coffee" ).getAmount() );
        Assertions.assertEquals( 500, (int) ivt.getIngredient( "Milk" ).getAmount() );
        Assertions.assertEquals( 500, (int) ivt.getIngredient( "Sugar" ).getAmount() );

        // test with not enough coffee
        final Ingredient chocolate3 = new Ingredient( "Chocolate", 500 );
        final Ingredient coffee3 = new Ingredient( "Coffee", 0 );
        final Ingredient milk3 = new Ingredient( "Milk", 500 );
        final Ingredient sugar3 = new Ingredient( "Sugar", 500 );
        final List<Ingredient> ingredients3 = new ArrayList<Ingredient>();
        ingredients3.add( chocolate3 );
        ingredients3.add( coffee3 );
        ingredients3.add( milk3 );
        ingredients3.add( sugar3 );
        ivt.setIngredients( ingredients3 );

        Assertions.assertFalse( ivt.useIngredients( recipe ) );
        Assertions.assertEquals( 500, (int) ivt.getIngredient( "Chocolate" ).getAmount() );
        Assertions.assertEquals( 0, (int) ivt.getIngredient( "Coffee" ).getAmount() );
        Assertions.assertEquals( 500, (int) ivt.getIngredient( "Milk" ).getAmount() );
        Assertions.assertEquals( 500, (int) ivt.getIngredient( "Sugar" ).getAmount() );

        // test with not enough milk
        final Ingredient chocolate4 = new Ingredient( "Chocolate", 500 );
        final Ingredient coffee4 = new Ingredient( "Coffee", 500 );
        final Ingredient milk4 = new Ingredient( "Milk", 0 );
        final Ingredient sugar4 = new Ingredient( "Sugar", 500 );
        final List<Ingredient> ingredients4 = new ArrayList<Ingredient>();
        ingredients4.add( chocolate4 );
        ingredients4.add( coffee4 );
        ingredients4.add( milk4 );
        ingredients4.add( sugar4 );
        ivt.setIngredients( ingredients4 );

        Assertions.assertFalse( ivt.useIngredients( recipe ) );
        Assertions.assertEquals( 500, (int) ivt.getIngredient( "Chocolate" ).getAmount() );
        Assertions.assertEquals( 500, (int) ivt.getIngredient( "Coffee" ).getAmount() );
        Assertions.assertEquals( 0, (int) ivt.getIngredient( "Milk" ).getAmount() );
        Assertions.assertEquals( 500, (int) ivt.getIngredient( "Sugar" ).getAmount() );

        // test with not enough sugar
        final Ingredient chocolate5 = new Ingredient( "Chocolate", 500 );
        final Ingredient coffee5 = new Ingredient( "Coffee", 500 );
        final Ingredient milk5 = new Ingredient( "Milk", 500 );
        final Ingredient sugar5 = new Ingredient( "Sugar", 0 );
        final List<Ingredient> ingredients5 = new ArrayList<Ingredient>();
        ingredients5.add( chocolate5 );
        ingredients5.add( coffee5 );
        ingredients5.add( milk5 );
        ingredients5.add( sugar5 );
        ivt.setIngredients( ingredients5 );

        Assertions.assertFalse( ivt.useIngredients( recipe ) );
        Assertions.assertEquals( 500, (int) ivt.getIngredient( "Chocolate" ).getAmount() );
        Assertions.assertEquals( 500, (int) ivt.getIngredient( "Coffee" ).getAmount() );
        Assertions.assertEquals( 500, (int) ivt.getIngredient( "Milk" ).getAmount() );
        Assertions.assertEquals( 0, (int) ivt.getIngredient( "Sugar" ).getAmount() );

    }

    /**
     * Tests adding ingredients to inventory.
     */
    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();

        final Ingredient coffee = new Ingredient( "Coffee", 5 );
        final Ingredient milk = new Ingredient( "Milk", 3 );
        final Ingredient sugar = new Ingredient( "Sugar", 7 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 2 );

        ingredients.add( coffee );
        ingredients.add( milk );
        ingredients.add( sugar );
        ingredients.add( chocolate );

        ivt.addIngredients( ingredients );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, (int) ivt.getIngredient( "Coffee" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 503, (int) ivt.getIngredient( "Milk" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, (int) ivt.getIngredient( "Sugar" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 502, (int) ivt.getIngredient( "Chocolate" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values chocolate" );

    }

    /**
     * Tests adding ingredients to inventory.
     */
    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            final Ingredient coffee = new Ingredient( "Coffee", -5 );
            final Ingredient milk = new Ingredient( "Milk", 3 );
            final Ingredient sugar = new Ingredient( "Sugar", 7 );
            final Ingredient chocolate = new Ingredient( "Chocolate", 2 );
            final List<Ingredient> ingredients = new ArrayList<Ingredient>();
            ingredients.add( coffee );
            ingredients.add( milk );
            ingredients.add( sugar );
            ingredients.add( chocolate );

            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Coffee" ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
        }
    }

    /**
     * Tests adding ingredients to inventory.
     */
    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            final Ingredient coffee = new Ingredient( "Coffee", 5 );
            final Ingredient milk = new Ingredient( "Milk", -3 );
            final Ingredient sugar = new Ingredient( "Sugar", 7 );
            final Ingredient chocolate = new Ingredient( "Chocolate", 2 );
            final List<Ingredient> ingredients = new ArrayList<Ingredient>();
            ingredients.add( coffee );
            ingredients.add( milk );
            ingredients.add( sugar );
            ingredients.add( chocolate );

            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Milk" ).getAmount(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk" );

        }

    }

    /**
     * Tests adding ingredients to inventory.
     */
    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            final Ingredient coffee = new Ingredient( "Coffee", 5 );
            final Ingredient milk = new Ingredient( "Milk", 3 );
            final Ingredient sugar = new Ingredient( "Sugar", -7 );
            final Ingredient chocolate = new Ingredient( "Chocolate", 2 );
            final List<Ingredient> ingredients = new ArrayList<Ingredient>();
            ingredients.add( coffee );
            ingredients.add( milk );
            ingredients.add( sugar );
            ingredients.add( chocolate );

            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Sugar" ).getAmount(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar" );
        }

    }

    /**
     * Tests adding ingredients to inventory.
     */
    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            final Ingredient coffee = new Ingredient( "Coffee", 5 );
            final Ingredient milk = new Ingredient( "Milk", 3 );
            final Ingredient sugar = new Ingredient( "Sugar", 7 );
            final Ingredient chocolate = new Ingredient( "Chocolate", -2 );
            final List<Ingredient> ingredients = new ArrayList<Ingredient>();
            ingredients.add( coffee );
            ingredients.add( milk );
            ingredients.add( sugar );
            ingredients.add( chocolate );

            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Chocolate" ).getAmount(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate" );

        }

    }

    /**
     * Tests checkIngredient method.
     */
    @Test
    @Transactional
    public void testCheckIngredients () {
        final Inventory ivt = inventoryService.getInventory();

        // Check that negative numbers throw errors
        final Exception e1 = Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.checkIngredient( "Coffee", "-1" ) );
        Assertions.assertEquals( "Units of Coffee must be a positive integer", e1.getMessage() );

        final Exception e2 = Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.checkIngredient( "Milk", "-1" ) );
        Assertions.assertEquals( "Units of Milk must be a positive integer", e2.getMessage() );

        final Exception e3 = Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.checkIngredient( "Sugar", "-1" ) );
        Assertions.assertEquals( "Units of Sugar must be a positive integer", e3.getMessage() );

        final Exception e4 = Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.checkIngredient( "Chocolate", "-1" ) );
        Assertions.assertEquals( "Units of Chocolate must be a positive integer", e4.getMessage() );

        // Check that non-numbers throw errors
        final Exception e5 = Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.checkIngredient( "Coffee", "a" ) );
        Assertions.assertEquals( "Units of Coffee must be a positive integer", e5.getMessage() );

        final Exception e6 = Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.checkIngredient( "Milk", "a" ) );
        Assertions.assertEquals( "Units of Milk must be a positive integer", e6.getMessage() );

        final Exception e7 = Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.checkIngredient( "Sugar", "a" ) );
        Assertions.assertEquals( "Units of Sugar must be a positive integer", e7.getMessage() );

        final Exception e8 = Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.checkIngredient( "Chocolate", "a" ) );
        Assertions.assertEquals( "Units of Chocolate must be a positive integer", e8.getMessage() );

    }

    @Test
    @Transactional
    public void testIDs () {
        final Inventory ivt = inventoryService.getInventory();

        Assertions.assertEquals( 0, ivt.getIncrNextGuestID() );
        Assertions.assertEquals( 0, ivt.getIncrNextOrderID() );

        Assertions.assertEquals( 1, ivt.getIncrNextGuestID() );
        Assertions.assertEquals( 1, ivt.getIncrNextOrderID() );

    }

    /**
     * Tests the toString() method.
     */
    @Test
    @Transactional
    public void testToString () {
        final Inventory ivt = inventoryService.getInventory();

        final Ingredient chocolate1 = new Ingredient( "Chocolate", 500 );
        final Ingredient coffee1 = new Ingredient( "Coffee", 500 );
        final Ingredient milk1 = new Ingredient( "Milk", 500 );
        final Ingredient sugar1 = new Ingredient( "Sugar", 500 );

        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( chocolate1 );
        ingredients.add( coffee1 );
        ingredients.add( sugar1 );
        ingredients.add( milk1 );

        ivt.setIngredients( ingredients );

        // See if inventory is correctly printed out
        Assertions.assertEquals( "Chocolate: 500\nCoffee: 500\nSugar: 500\nMilk: 500\n", ivt.toString() );

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        // Create Ingredients
        final Ingredient chocolate = new Ingredient( "Chocolate", 10 );
        final Ingredient milk = new Ingredient( "Milk", 20 );
        final Ingredient sugar = new Ingredient( "Sugar", 5 );
        final Ingredient coffee = new Ingredient( "Coffee", 1 );
        // Add ingredients to recipe
        recipe.addIngredient( chocolate );
        recipe.addIngredient( milk );
        recipe.addIngredient( sugar );
        recipe.addIngredient( coffee );

        recipe.setPrice( 5 );

        ivt.useIngredients( recipe );

        // See if inventory is correctly updated
        Assertions.assertEquals( "Chocolate: 490\nCoffee: 499\nSugar: 495\nMilk: 480\n", ivt.toString() );

    }

}
