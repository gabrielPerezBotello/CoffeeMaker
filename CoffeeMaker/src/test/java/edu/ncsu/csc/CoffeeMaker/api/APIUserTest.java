package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIUserTest { // begin class{}.

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService           service;

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
    public void testAddBarista () throws Exception {

        service.deleteAll();

        // Test adding a barista that does not exist in the system.
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON ).content( "barista1" ) )
                .andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );

        User barista1 = service.findByUsername( "barista1" );
        Assertions.assertNotEquals( null, barista1.getUsername() );

        Assertions.assertEquals( "barista1", barista1.getUsername() );
        Assertions.assertNotEquals( "", barista1.getPassword() );

        // Test adding a barista that already exists in the system.
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON ).content( "barista1" ) )
                .andExpect( status().isConflict() );

        Assertions.assertEquals( 1, (int) service.count() );

        barista1 = service.findByUsername( "barista1" );
        Assertions.assertNotEquals( null, barista1.getUsername() );

        Assertions.assertEquals( "barista1", barista1.getUsername() );
        Assertions.assertNotEquals( "", barista1.getPassword() );

    }

    @Test
    @Transactional
    public void testRemoveBarista () throws Exception {

        service.deleteAll();

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON ).content( "barista1" ) )
                .andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );

        final User barista1 = service.findByUsername( "barista1" );
        Assertions.assertNotEquals( null, barista1.getUsername() );

        Assertions.assertEquals( "barista1", barista1.getUsername() );
        Assertions.assertNotEquals( "", barista1.getPassword() );

        /* BEGIN CRITICAL SECTION FOR TESTING removeBarista(). */

        // Test removing a barista that does not exist in the system.
        mvc.perform( delete( "/api/v1/users/nonexistent" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );

        Assertions.assertEquals( 1, (int) service.count() );

        // Test removing a barista that does exist in the system.
        mvc.perform( delete( "/api/v1/users/barista1" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        Assertions.assertEquals( 0, (int) service.count() );

    }

} // end class{}.
