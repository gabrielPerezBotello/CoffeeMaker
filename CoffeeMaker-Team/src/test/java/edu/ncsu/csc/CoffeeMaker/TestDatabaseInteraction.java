package edu.ncsu.csc.CoffeeMaker;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {

    @Autowired
    private RecipeService recipeService;

    @Test
    @Transactional
    public void testRecipes () {
        recipeService.deleteAll();

        // Test Add Functionality
        final Recipe r = new Recipe();

        r.setName( "Covfefe" );

        // Create Ingredients
        final Ingredient chocolate = new Ingredient( "Chocolate", 10 );
        final Ingredient milk = new Ingredient( "Milk", 20 );
        final Ingredient sugar = new Ingredient( "Sugar", 5 );
        final Ingredient coffee = new Ingredient( "Coffee", 1 );
        // Add ingredients to recipe
        r.addIngredient( chocolate );
        r.addIngredient( milk );
        r.addIngredient( sugar );
        r.addIngredient( coffee );
        r.setPrice( 2 );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        Assertions.assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        Assertions.assertEquals( r.getName(), dbRecipe.getName() );
        Assertions.assertEquals( r.getIngredient( "Chocolate" ).getAmount(),
                dbRecipe.getIngredient( "Chocolate" ).getAmount() );
        Assertions.assertEquals( r.getIngredient( "Sugar" ).getAmount(),
                dbRecipe.getIngredient( "Sugar" ).getAmount() );
        Assertions.assertEquals( r.getIngredient( "Milk" ).getAmount(), dbRecipe.getIngredient( "Milk" ).getAmount() );
        Assertions.assertEquals( r.getIngredient( "Coffee" ).getAmount(),
                dbRecipe.getIngredient( "Coffee" ).getAmount() );
        Assertions.assertEquals( r.getPrice(), dbRecipe.getPrice() );

        Assertions.assertEquals( r, recipeService.findByName( "Covfefe" ) );

        // Test Edit functionality
        // dbRecipe.setName( "Coffee" );
        // dbRecipe.setPrice( 15 );
        // dbRecipe.setSugar( 12 );
        // dbRecipe.setCoffee( 3 );
        // dbRecipe.setMilk( 2 );
        // dbRecipe.setChocolate( 10 );
        // recipeService.save( dbRecipe );
        //
        // Assertions.assertEquals( 1, recipeService.count() );
        //
        // Assertions.assertEquals( "Coffee", recipeService.findAll().get( 0
        // ).getName() );
        // Assertions.assertEquals( 15, (int) recipeService.findAll().get( 0
        // ).getPrice() );
        // Assertions.assertEquals( 12, (int) recipeService.findAll().get( 0
        // ).getSugar() );
        // Assertions.assertEquals( 3, (int) recipeService.findAll().get( 0
        // ).getCoffee() );
        // Assertions.assertEquals( 2, (int) recipeService.findAll().get( 0
        // ).getMilk() );
        // Assertions.assertEquals( 10, (int) recipeService.findAll().get( 0
        // ).getChocolate() );

        // Test Delete functionality
        Assertions.assertEquals( 1, recipeService.count(), "There should be one recipe in the database" );

        recipeService.delete( r ); // Delete the recipe
        Assertions.assertEquals( 0, recipeService.findAll().size(), "There should be no recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        recipeService.save( r1 );
        final Recipe r2 = new Recipe();
        recipeService.save( r2 );
        final Recipe r3 = new Recipe();
        recipeService.save( r3 );

        Assertions.assertEquals( 3, recipeService.count(), "There should be three recipes in the database" );

        recipeService.deleteAll(); // Delete all recipes
        Assertions.assertEquals( 0, recipeService.findAll().size(), "There should be no recipes in the CoffeeMaker" );

        recipeService.delete( r ); // Try and delete a recipe that has already
        // been deleted
        Assertions.assertEquals( 0, recipeService.findAll().size(), "There should be no recipes in the CoffeeMaker" );

        recipeService.save( r1 );
        Assertions.assertEquals( 1, recipeService.findAll().size(), "There should be one recipe in the CoffeeMaker" );

        recipeService.delete( r ); // Do it again but with a different recipe in
        // the system

        Assertions.assertEquals( 1, recipeService.findAll().size(), "There should be no recipes in the CoffeeMaker" );

    }
}
