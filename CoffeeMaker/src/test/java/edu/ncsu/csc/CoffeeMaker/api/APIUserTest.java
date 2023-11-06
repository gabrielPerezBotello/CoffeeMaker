package edu.ncsu.csc.CoffeeMaker.api;

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

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.UserRole;
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

        // Test removing a barista that does not exist in the system.
        mvc.perform( delete( "/api/v1/users/nonexistent" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );

        Assertions.assertEquals( 1, (int) service.count() );

        // Test removing a barista that does exist in the system.
        mvc.perform( delete( "/api/v1/users/barista1" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        Assertions.assertEquals( 0, (int) service.count() );

    }

    @Test
    @Transactional
    public void testGetBaristas () throws Exception { // begin method().

        service.deleteAll();

        // Test getting all baristas when there are no baristas in the system.
        final String result = mvc.perform( get( "/api/v1/users/baristas" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        Assertions.assertEquals( "[]", result );

        // Assertions.assertEquals( 1, (int) service.count() );

        // Add 3 baristas into the system.
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON ).content( "barista1" ) )
                .andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON ).content( "barista2" ) )
                .andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON ).content( "barista3" ) )
                .andExpect( status().isOk() );

        Assertions.assertEquals( 3, (int) service.count() );

        // Test getting all baristas when there are 3 baristas in the system.
        final String result2 = mvc.perform( get( "/api/v1/users/baristas" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        Assertions.assertFalse( result2.contains( "\"role\":\"CUSTOMER\"" ) );
        Assertions.assertFalse( result2.contains( "\"role\":\"MANAGER\"" ) );

        Assertions.assertTrue( result2.contains( "\"role\":\"BARISTA\"" ) );
        Assertions.assertTrue( result2.contains( "\"username\":\"barista1" ) );
        Assertions.assertTrue( result2.contains( "\"username\":\"barista2" ) );
        Assertions.assertTrue( result2.contains( "\"username\":\"barista3" ) );

        // Add 2 customers into the system.
        final User customer1 = new User( "customer1", "cpass1", UserRole.CUSTOMER );
        service.save( customer1 );

        final User customer2 = new User( "customer2", "cpass2", UserRole.CUSTOMER );
        service.save( customer2 );

        // Add a manager into the system.
        final User manager1 = new User( "manager1", "mpass1", UserRole.MANAGER );
        service.save( manager1 );

        // Test getting all baristas when there are 3 baristas, 2 customers, and
        // 1 manager in the system.
        final String result3 = mvc.perform( get( "/api/v1/users/baristas" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        Assertions.assertFalse( result3.contains( "\"role\":\"CUSTOMER\"" ) );
        Assertions.assertFalse( result3.contains( "\"role\":\"MANAGER\"" ) );
        Assertions.assertFalse( result3.contains( "\"username\":\"customer1\"" ) );
        Assertions.assertFalse( result3.contains( "\"password\":\"cpass1\"" ) );
        Assertions.assertFalse( result3.contains( "\"username\":\"customer2\"" ) );
        Assertions.assertFalse( result3.contains( "\"password\":\"cpass2\"" ) );
        Assertions.assertFalse( result3.contains( "\"username\":\"manager1\"" ) );
        Assertions.assertFalse( result3.contains( "\"password\":\"mpass1\"" ) );

        Assertions.assertTrue( result3.contains( "\"role\":\"BARISTA\"" ) );
        Assertions.assertTrue( result3.contains( "\"username\":\"barista1" ) );
        Assertions.assertTrue( result3.contains( "\"username\":\"barista2" ) );
        Assertions.assertTrue( result3.contains( "\"username\":\"barista3" ) );

        // Assertions.assertEquals( "[]", result2 );

    } // end method().

    @Test
    @Transactional
    public void testAuthenticate () throws Exception { // begin method().

        service.deleteAll();

        // Add a customer into the system.
        final User customer1 = new User( "customer1", "cpass1", UserRole.CUSTOMER );
        service.save( customer1 );

        Assertions.assertEquals( 1, (int) service.count() );

        // Test authenticating with a username that is not associated with any
        // Users in the system.
        mvc.perform( get( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON ).content( "user:pass" ) )
                .andExpect( status().isUnauthorized() );

        mvc.perform( get( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON ).content( "user:cpass1" ) )
                .andExpect( status().isUnauthorized() );

        // Test authenticating with a username that is associated with a User in
        // the system, and a password that does NOT match that of the associated
        // User.
        mvc.perform( get( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON ).content( "customer1:pass" ) )
                .andExpect( status().isUnauthorized() );

        // Test authenticating with a username that is associated with a User in
        // the system, and a password that matches that of the associated User.
        mvc.perform( get( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON ).content( "customer1:cpass1" ) )
                .andExpect( status().isOk() );

    } // end method().

}
// end class{}.
