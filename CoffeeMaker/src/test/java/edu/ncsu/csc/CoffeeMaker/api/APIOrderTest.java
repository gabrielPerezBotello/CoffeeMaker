package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
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
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.CustomerOrderService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIOrderTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CustomerOrderService  service;

    @Autowired
    private InventoryService      iService;

    /**
     * RecipeService object to be used in testing.
     */
    @Autowired
    private RecipeService         recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    @SuppressWarnings ( "unchecked" )
    @Test
    @Transactional
    public void ensureOrder () throws Exception {
        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setPrice( 0 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) ).andExpect( status().isOk() );

        final CustomerOrder o = new CustomerOrder( "sharon", recipeService.findByName( "Delicious Not-Coffee" ) );
        System.out.println( TestUtils.asJsonString( o ) );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );

        assertTrue( mvc.perform( get( "/api/v1/orders" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString().contains( "\"customerName\":\"sharon\"" ) );

        assertTrue( mvc
                .perform( get( "/api/v1/orders/sharon" ).contentType( MediaType.APPLICATION_JSON ).content( "sharon" ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString()
                .contains( "\"customerName\":\"sharon\"" ) );
    }

    @Test
    @Transactional
    public void testDeleteOrder () throws Exception {
        /* Tests delete recipe api */
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setPrice( 0 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) ).andExpect( status().isOk() );

        CustomerOrder o = new CustomerOrder( "sharon", recipeService.findByName( "Delicious Not-Coffee" ) );
        System.out.println( TestUtils.asJsonString( o ) );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should be 1 Order in the CoffeeMaker" );
        o = service.findByCustomerName( "sharon" );

        final Long id = o.getId();

        mvc.perform( delete( "/api/v1/orders/" + id.toString() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( id ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );
        final Recipe recipe = new Recipe();

        recipe.setName( "Delicious Not-Coffee" );
        recipe.setPrice( 0 );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) ).andExpect( status().isOk() );

        final CustomerOrder o = new CustomerOrder( "sharon", recipeService.findByName( "Delicious Not-Coffee" ) );
        System.out.println( TestUtils.asJsonString( o ) );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should be 1 Order in the CoffeeMaker" );

        final Recipe recipe2 = new Recipe();
        recipe2.setName( "Delicious Not-Coffee2" );
        recipe2.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ) ).andExpect( status().isOk() );

        final CustomerOrder o2 = new CustomerOrder( "Jamie", recipeService.findByName( "Delicious Not-Coffee" ) );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o2 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 2, service.findAll().size(), "There should be 2 Order in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testAdvanceOrder () throws Exception {
        // Add recipe to database
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setPrice( 0 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) ).andExpect( status().isOk() );
        // Add customer order to database
        CustomerOrder o = new CustomerOrder( "sharon", recipeService.findByName( "Delicious Not-Coffee" ) );
        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o ) ) ).andExpect( status().isOk() );

        o = service.findByCustomerName( "sharon" );
        final Long id = o.getId();

        Assertions.assertEquals( 1, service.findAll().size(), "There should be 1 Order in the CoffeeMaker" );

        mvc.perform( put( "/api/v1/orders/fulfill/" + id.toString() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( id ) ) ).andExpect( status().isOk() );

        mvc.perform( put( "/api/v1/orders/pickup/" + id.toString() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( id ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testInventoryUpdates () throws Exception {
        // Add recipe to database
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setPrice( 0 );
        final Ingredient coffee = new Ingredient( "Coffee", 5 );
        final Ingredient sugar = new Ingredient( "Sugar", 5 );
        recipe.addIngredient( coffee );
        recipe.addIngredient( sugar );

        // Create initial Inventory
        final Ingredient coffee1 = new Ingredient( "Coffee", 50 );
        final Ingredient sugar1 = new Ingredient( "Sugar", 50 );
        final List<Ingredient> list = new ArrayList<Ingredient>();
        list.add( coffee1 );
        list.add( sugar1 );
        final Inventory inventory = new Inventory( list );

        iService.save( inventory );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) ).andExpect( status().isOk() );
        // Add customer order to database
        CustomerOrder o = new CustomerOrder( "sharon", recipeService.findByName( "Delicious Not-Coffee" ) );
        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o ) ) ).andExpect( status().isOk() );

        o = service.findByCustomerName( "sharon" );
        final Long id = o.getId();

        Assertions.assertEquals( 1, service.findAll().size(), "There should be 1 Order in the CoffeeMaker" );

        Inventory inv = iService.getInventory();

        Assertions.assertEquals( 50, inv.getIngredient( "Coffee" ).getAmount() );
        Assertions.assertEquals( 50, inv.getIngredient( "Sugar" ).getAmount() );

        mvc.perform( put( "/api/v1/orders/fulfill/" + id.toString() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( id ) ) ).andExpect( status().isOk() );

        inv = iService.getInventory();

        Assertions.assertEquals( 45, inv.getIngredient( "Coffee" ).getAmount() );
        Assertions.assertEquals( 45, inv.getIngredient( "Sugar" ).getAmount() );
    }

}
