package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
     * is used to create a new User (Barista) by automatically converting the
     * JSON RequestBody provided to a User (Barista) object. Invalid JSON will
     * fail.
     *
     * @param user
     *            The valid User to be saved.
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

} // end class{}.
