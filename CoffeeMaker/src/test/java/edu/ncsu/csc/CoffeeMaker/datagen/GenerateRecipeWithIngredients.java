package edu.ncsu.csc.CoffeeMaker.datagen;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * The GenerateRecipeWithIngredients class.
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateRecipeWithIngredients {

    /**
     * The RecipeService object used for testing.
     */
    @Autowired
    private RecipeService recipeService;

    /**
     * Sets up testing environment
     */
    @Before
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * Helper method for tests
     */
    @Test
    @Transactional
    public void createRecipe () {
        final Recipe r1 = new Recipe();
        r1.setName( "Delicious Coffee" );

        r1.setPrice( 50 );

        r1.addIngredient( new Ingredient( "Coffee", 10 ) );
        r1.addIngredient( new Ingredient( "Pumpkin Spice", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 2 ) );

        recipeService.save( r1 );

        printRecipes();
    }

    private void printRecipes () {
        for ( final DomainObject r : recipeService.findAll() ) {
            System.out.println( r );
        }
    }

}
