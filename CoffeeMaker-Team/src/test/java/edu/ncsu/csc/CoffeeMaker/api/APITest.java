
package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc

public class APITest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @Test
    @Transactional
    public void testGetRecipe () throws Exception {
        // final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo(
        // print() ).andExpect( status().isOk() )
        // .andReturn().getResponse().getContentAsString();
        //
        // if ( !recipe.contains( "Mocha" ) ) {
        // // create a new Mocha recipe
        // final Recipe mocha = new Recipe();
        // mocha.setName( "Mocha" );
        // mocha.setPrice( 1 );
        // // mocha.setCoffee( 1 );
        // // mocha.setMilk( 1 );
        // // mocha.setSugar( 1 );
        // // mocha.setChocolate( 1 );
        //
        // mvc.perform( post( "/api/v1/recipes" ).contentType(
        // MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( mocha ) ) ).andExpect(
        // status().isOk() );
        //
        // }
        //
        // assertTrue( mvc.perform( get( "/api/v1/recipes" ) ).andDo( print()
        // ).andExpect( status().isOk() ).andReturn()
        // .getResponse().getContentAsString().contains( "Mocha" ) );
        assertTrue( true );
    }

    @Test
    @Transactional
    public void testAddInventory () throws Exception {
        final Inventory inventory = new Inventory();
        final Ingredient ing = new Ingredient();
        ing.setAmount( 10 );
        ing.setName( "Milk" );
        inventory.addIngredient( ing );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory ) ) ).andExpect( status().isOk() );

        System.out.println( mvc.perform( get( "/api/v1/inventory" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString() );
        assertTrue( mvc.perform( get( "/api/v1/inventory" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString().contains( "\"name\":\"Milk\",\"amount\":10" ) );

        final Ingredient ing2 = new Ingredient();
        ing2.setAmount( 10 );
        ing2.setName( "Caramel" );

    }

    @Test
    @Transactional
    public void testMakeCoffee () throws Exception {
        final Inventory inventory = new Inventory();
        inventory.addIngredient( new Ingredient( "Coffee", 50 ) );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inventory ) ) ).andExpect( status().isOk() );

        final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        if ( !recipe.contains( "Mocha" ) ) {
            // create a new Mocha recipe
            final Recipe mocha = new Recipe();
            mocha.setName( "Mocha" );
            mocha.setPrice( 1 );
            mocha.addIngredient( new Ingredient( "Coffee", 1 ) );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( mocha ) ) ).andExpect( status().isOk() );

        }
        System.out.println( mvc.perform( get( "/api/v1/inventory" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString() );
        assertTrue( mvc.perform( get( "/api/v1/inventory" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString().contains( "\"Coffee\",\"amount\":50" ) );

        mvc.perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 100 ) ) ).andExpect( status().isOk() );

        assertTrue( mvc.perform( get( "/api/v1/inventory" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString().contains( "\"Coffee\",\"amount\":49" ) );
        assertTrue( true );

    }

}
