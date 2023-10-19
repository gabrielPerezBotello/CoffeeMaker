package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.validation.ConstraintViolationException;

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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests Recipe Class.
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {
    /**
     * RecipeService object to be used in testing.
     */
    @Autowired
    private RecipeService service;

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
    public void testAddRecipe () {

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
        // Save recipe
        service.save( r1 );

        // create new ingredients
        final Ingredient coffee2 = new Ingredient( "Coffee", 1 );
        final Ingredient milk2 = new Ingredient( "Milk", 1 );
        final Ingredient sugar2 = new Ingredient( "Sugar", 1 );
        final Ingredient chocolate2 = new Ingredient( "Chocolate", 1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        // Add Ingredients
        r2.addIngredient( coffee2 );
        r2.addIngredient( milk2 );
        r2.addIngredient( sugar2 );
        r2.addIngredient( chocolate2 );
        // Save recipe
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        // Create Ingredients
        final Ingredient coffee1 = new Ingredient( "Coffee", -12 );
        final Ingredient milk1 = new Ingredient( "Milk", 0 );
        final Ingredient sugar1 = new Ingredient( "Sugar", 0 );
        final Ingredient chocolate1 = new Ingredient( "Chocolate", 0 );
        // Add Ingredients to Recipe
        r1.addIngredient( coffee1 );
        r1.addIngredient( milk1 );
        r1.addIngredient( sugar1 );
        r1.addIngredient( chocolate1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        // Create Ingredients
        final Ingredient coffee2 = new Ingredient( "Coffee", 1 );
        final Ingredient milk2 = new Ingredient( "Milk", 1 );
        final Ingredient sugar2 = new Ingredient( "Sugar", 1 );
        final Ingredient chocolate2 = new Ingredient( "Chocolate", 1 );
        // Add Ingredients to Recipe
        r2.addIngredient( coffee2 );
        r2.addIngredient( milk2 );
        r2.addIngredient( sugar2 );
        r2.addIngredient( chocolate2 );

        final List<Recipe> recipes = List.of( r1, r2 );

        try {
            service.saveAll( recipes );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, 3, 1, 1, 0 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, -3, 1, 1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of coffee" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, -1, 1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of milk" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, -1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of sugar" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe7 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, -2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of chocolate" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    /**
     * Tests editing a recipe
     */
    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        Assertions.assertEquals( 3, (int) retrieved.getIngredient( "Coffee" ).getAmount() );
        Assertions.assertEquals( 1, (int) retrieved.getIngredient( "Milk" ).getAmount() );
        Assertions.assertEquals( 1, (int) retrieved.getIngredient( "Sugar" ).getAmount() );
        Assertions.assertEquals( 0, (int) retrieved.getIngredient( "Chocolate" ).getAmount() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

    // @Test
    // @Transactional
    // public void testUpdateRecipe () {
    // final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
    // final Recipe r2 = createRecipe( "Mocha", 0, 1, 1, 3, 50 );
    // r1.updateRecipe( r2 );
    //
    // // Assertions.assertEquals( 50, r1.getChocolate() );
    // // Assertions.assertEquals( 1, r1.getCoffee() );
    // // Assertions.assertEquals( 1, r1.getMilk() );
    // // Assertions.assertEquals( 3, r1.getSugar() );
    // Assertions.assertEquals( 0, r1.getPrice() );
    // }

    @Test
    @Transactional
    public void testCheckRecipe () {
        // final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        // final Recipe r2 = createRecipe( "Coffee", 50, 0, 1, 0, 0 );
        final Recipe r3 = createRecipe( "Coffee", 50, 0, 0, 0, 0 );
        // final Recipe r4 = createRecipe( "Coffee", 50, 0, 0, 1, 0 );
        // final Recipe r5 = createRecipe( "Coffee", 50, 3, 0, 0, 0 );
        // final Recipe r6 = createRecipe( "Coffee", 50, 0, 0, 0, 4 );

        // Assertions.assertFalse( r1.checkRecipe() );
        // Assertions.assertFalse( r2.checkRecipe() );
        Assertions.assertTrue( r3.checkRecipe() );
        // Assertions.assertFalse( r4.checkRecipe() );
        // Assertions.assertFalse( r5.checkRecipe() );
        // Assertions.assertFalse( r6.checkRecipe() );

    }

    @SuppressWarnings ( "unlikely-arg-type" )
    @Test
    @Transactional
    public void testEqualRecipe () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        final Recipe r2 = createRecipe( null, 50, 3, 1, 1, 0 );
        final Recipe r3 = createRecipe( "Mocha", 50, 3, 1, 1, 0 );
        final String s = "not a Recipe object";
        service.save( r1 );
        service.save( r2 );
        service.save( r3 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertTrue( retrieved.equals( r1 ) );
        Assertions.assertFalse( retrieved.equals( r2 ) );
        Assertions.assertFalse( retrieved.equals( r3 ) );
        Assertions.assertFalse( r2.equals( r3 ) );
        Assertions.assertFalse( retrieved.equals( s ) );
        Assertions.assertFalse( retrieved.equals( null ) );
        Assertions.assertFalse( r3.equals( r2 ) );

    }

    @Test
    @Transactional
    public void testToStringHash () {
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        final Recipe r2 = createRecipe( null, 50, 3, 1, 1, 0 );

        Assertions.assertEquals( "Coffee", r1.getName() );
        Assertions.assertEquals( "Coffee: Coffee=3 Milk=1 Sugar=1 Chocolate=0 ", r1.toString() );
        Assertions.assertEquals( r1.hashCode(), r1.hashCode() );
        Assertions.assertEquals( 31, r2.hashCode() );
    }

    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        // Create Ingredients
        final Ingredient coffee1 = new Ingredient( "Coffee", coffee );
        final Ingredient milk1 = new Ingredient( "Milk", milk );
        final Ingredient sugar1 = new Ingredient( "Sugar", sugar );
        final Ingredient chocolate1 = new Ingredient( "Chocolate", chocolate );
        // Add Ingredients to Recipe
        recipe.addIngredient( coffee1 );
        recipe.addIngredient( milk1 );
        recipe.addIngredient( sugar1 );
        recipe.addIngredient( chocolate1 );

        return recipe;
    }

}
