package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.UserRole;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Users.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author vmnair
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIUserController extends APIController { // begin class{}.

    /**
     * UserService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private UserService service;

    /**
     * REST API method to provide POST access to the User (Barista) model. This
     * is used to create a new User (Barista) by creating a User (Barista)
     * object with the provided username. Invalid JSON will fail.
     *
     * @param username
     *            The username for the Barista to be added
     * @return ResponseEntity indicating success if the User could be saved to
     *         the database, or an error if it could not be.
     */
    @PostMapping ( BASE_PATH + "/users" )
    public ResponseEntity addBarista ( @RequestBody final String username ) { // begin
        // method().

        if ( null != service.findByUsername( username ) ) { // begin if.

            return new ResponseEntity( errorResponse( "User with the name " + username + " already exists" ),
                    HttpStatus.CONFLICT );

        } // end if.

        final String password = UUID.randomUUID().toString().substring( 0, 8 );

        final User newBarista = new User( username, password, UserRole.BARISTA );

        service.save( newBarista );

        return new ResponseEntity( newBarista, HttpStatus.OK );

    } // end method().

    /**
     * REST API method to allow deleting a User (Barista) from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the user to delete (as a path variable)
     *
     * @param name
     *            The username of the User (Barista) to delete
     * @return Success if the User (Barista) with the given username could be
     *         deleted; an error if the User (Barista) with the given username
     *         does not exist
     */
    @DeleteMapping ( BASE_PATH + "/users/{name}" )
    public ResponseEntity removeBarista ( @PathVariable final String name ) {
        final User barista = service.findByUsername( name );
        if ( null == barista ) {
            return new ResponseEntity( errorResponse( "No barista found for username " + name ), HttpStatus.NOT_FOUND );
        }

        service.delete( barista );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

    /**
     * REST API method to provide GET access to all baristas in the system.
     *
     * @return JSON representation of all baristas in the system.
     */
    @GetMapping ( BASE_PATH + "/users/baristas" )
    public List<User> getBaristas () {

        final List<User> allUsers = service.findAll();

        for ( int i = 0; i < allUsers.size(); i++ ) { // begin for.

            final User user = allUsers.get( i );

            if ( !UserRole.BARISTA.equals( user.getRole() ) ) { // begin if.

                allUsers.remove( i );
                i--;

            } // end if.

        } // end for.

        return allUsers;
    }

    /**
     * REST API method to provide GET access to the specific user with the
     * provided username and password (in request body) if existing. This is
     * used to authenticate an attempted login with the provided credentials.
     *
     * @param usernameColonPassword
     *            the Users's username + ":" + the User's password
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/users" )
    public ResponseEntity authenticate ( @RequestBody final String usernameColonPassword ) {

        final String username = usernameColonPassword.split( ":" )[0];
        final String password = usernameColonPassword.split( ":" )[1];

        final User user = service.findByUsername( username );

        // Check whether there is no User in the system with the given username.
        //
        if ( null == user ) { // begin if.

            return new ResponseEntity( errorResponse( "No user with username \"" + username + "\" exists." ),
                    HttpStatus.UNAUTHORIZED );

        } // end if.

        // There is a User in the system with the given username.

        // Check whether the given password does NOT match with the password of
        // the User in the system.
        //
        if ( !password.equals( user.getPassword() ) ) { // begin if.

            return new ResponseEntity( errorResponse( "No user with username \"" + username + "\" exists." ),
                    HttpStatus.UNAUTHORIZED );

        } // end if.

        // The provided username and password match the username and password of
        // the User in the system.

        return new ResponseEntity( HttpStatus.OK );

    }

    /**
     * REST API method to provide POST access to the User (Customer) model. This
     * is used to create a new User (Customer) by creating a User (Customer)
     * object with the provided username and password. Invalid JSON will fail.
     *
     * @param usernameColonPassword
     *            the customer's username + ":" + the customer's password
     * @return response to the request
     */
    @PostMapping ( BASE_PATH + "/users/customers" )
    public ResponseEntity createCustomerAccount ( @RequestBody final String usernameColonPassword ) {

        final String username = usernameColonPassword.split( ":" )[0];
        final String password = usernameColonPassword.split( ":" )[1];

        if ( null != service.findByUsername( username ) ) { // begin if.

            return new ResponseEntity( errorResponse( "User with the name " + username + " already exists" ),
                    HttpStatus.CONFLICT );

        } // end if.

        final User newCustomer = new User( username, password, UserRole.CUSTOMER );

        service.save( newCustomer );

        return new ResponseEntity( newCustomer, HttpStatus.OK );

    }

} // end class{}.
