package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * Tests the APIIngredient class
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIIngredientTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    /**
     * The WebApplicationContext object to be used in testing.
     */
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService     service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    @Test
    @Transactional
    public void ensureIngredient () throws Exception {
        try {
            service.deleteAll();
        }
        catch ( final Exception conEx ) {
            System.out.println( conEx );
        }

        final Ingredient i = new Ingredient();
        i.setAmount( 10 );
        i.setName( "Caramel" );
        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void ensureIngredientGet () throws Exception {
        try {
            service.deleteAll();
        }
        catch ( final Exception conEx ) {
            System.out.println( conEx );
        }

        final Ingredient i = new Ingredient();
        i.setAmount( 10 );
        i.setName( "Caramel" );
        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );
        System.out.println( mvc.perform( get( "/api/v1/ingredient" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString() );
        assertTrue( mvc.perform( get( "/api/v1/ingredient/Caramel" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString().contains( "\"name\":\"Caramel\",\"amount\":10" ) );

    }

    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Ingredient i = new Ingredient();
        i.setAmount( 10 );
        i.setName( "caramel" );
        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) );

        Assertions.assertEquals( 1, (int) service.count() );
        assertTrue( true );

    }

    @Test
    @Transactional
    public void testIngredientAPI () throws Exception {

        service.deleteAll();

        final Ingredient i = new Ingredient();
        i.setAmount( 10 );
        i.setName( "Caramel" );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        assertEquals( 1, service.findAll().size(), "There should be 1 Ingredient in the CoffeeMaker" );

        mvc.perform( delete( "/api/v1/ingredient/Caramel" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );
        assertTrue( true );
    }

    @Test
    @Transactional
    public void testIngredientAPIDuplicate () throws Exception {

        service.deleteAll();

        /*
         * Tests an ingredient with a duplicate name to make sure it's rejected
         */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );
        final Ingredient i = new Ingredient();
        i.setName( "caramel" );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        final Ingredient i2 = new Ingredient();
        i2.setName( "caramel" );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one ingredient in the CoffeeMaker" );

    }

}
