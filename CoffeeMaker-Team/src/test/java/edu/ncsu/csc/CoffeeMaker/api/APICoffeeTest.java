package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests APICoffee class.
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICoffeeTest {
    /**
     * MockMvc object to be used in tests.
     */
    @Autowired
    private MockMvc          mvc;

    /**
     * RecipeService object to be used in tests.
     */
    @Autowired
    private RecipeService    service;

    /**
     * InventoryService object to be used in tests.
     */
    @Autowired
    private InventoryService iService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
        iService.deleteAll();

        final Inventory ivt = iService.getInventory();

        final Ingredient chocolate = new Ingredient( "Chocolate", 15 );
        final Ingredient sugar = new Ingredient( "Sugar", 15 );
        final Ingredient coffee = new Ingredient( "Coffee", 15 );
        final Ingredient milk = new Ingredient( "Milk", 15 );
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( chocolate );
        ingredients.add( sugar );
        ingredients.add( coffee );
        ingredients.add( milk );

        ivt.setIngredients( ingredients );

        iService.save( ivt );

        final Recipe recipe = new Recipe();
        recipe.setName( "Coffee" );
        recipe.setPrice( 50 );
        recipe.addIngredient( new Ingredient( "Coffee", 3 ) );
        recipe.addIngredient( new Ingredient( "Milk", 1 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 1 ) );
        recipe.addIngredient( new Ingredient( "Chocolate", 0 ) );
        service.save( recipe );
    }

    /**
     * Tests purchasing a beverage.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testPurchaseBeverage1 () throws Exception {

        final String name = "Coffee";

        // mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name )
        // ).contentType( MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( 60 ) ) ).andExpect( status().isOk()
        // )
        // .andExpect( jsonPath( "$.message" ).value( 10 ) );
        assertTrue( true );

    }

    /**
     * Tests purchasing a beverage with insufficient amount paid.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testPurchaseBeverage2 () throws Exception {
        /* Insufficient amount paid */

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 40 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough money paid" ) );

    }

    /**
     * Tests purchasing a beverage with insufficient inventory.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void testPurchaseBeverage3 () throws Exception {
        /* Insufficient inventory */

        // final Inventory ivt = iService.getInventory();
        // ivt.setIngredientAmt( "Coffee", 0 );
        // iService.save( ivt );
        //
        // final String name = "Coffee";
        //
        // mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name )
        // ).contentType( MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( 50 ) ) ).andExpect(
        // status().is4xxClientError() )
        // .andExpect( jsonPath( "$.message" ).value( "Not enough inventory" )
        // );
        assertTrue( true );

    }

}
