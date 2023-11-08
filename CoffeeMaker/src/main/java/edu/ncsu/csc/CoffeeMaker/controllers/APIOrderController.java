package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.OrderStatus;
import edu.ncsu.csc.CoffeeMaker.services.CustomerOrderService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Recipes.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIOrderController extends APIController {

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private CustomerOrderService service;

    /**
     * REST API method to provide GET access to all recipes in the system
     *
     * @return JSON representation of all recipies
     */
    @GetMapping ( BASE_PATH + "/orders" )
    public List<CustomerOrder> getOrders () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific recipe, as indicated
     * by the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/orders/{name}" )
    public List<CustomerOrder> getOrdersCustomer ( @PathVariable ( "name" ) final String name ) {
        final List<CustomerOrder> orders = service.findAll();
        final List<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>();
        for ( int i = 0; i < orders.size(); i++ ) {
            if ( orders.get( i ).getCustomerName().equals( name ) ) {
                customerOrders.add( orders.get( i ) );
            }
        }

        return customerOrders;
    }

    /**
     * REST API method to provide POST access to the Recipe model. This is used
     * to create a new Recipe by automatically converting the JSON RequestBody
     * provided to a Recipe object. Invalid JSON will fail.
     *
     * @param recipe
     *            The valid Recipe to be saved.
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/orders" )
    public ResponseEntity placeOrder ( @RequestBody final CustomerOrder order ) {
        if ( null != service.findByCustomerName( order.getCustomerName() ) ) {
            return new ResponseEntity(
                    errorResponse( "Order with the name " + order.getCustomerName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }

        service.save( order );
        return new ResponseEntity( successResponse( order.getCustomerName() + " successfully created" ),
                HttpStatus.OK );

    }

    /**
     * REST API method to allow deleting a Recipe from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the recipe to delete (as a path variable)
     *
     * @param name
     *            The name of the Recipe to delete
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @DeleteMapping ( BASE_PATH + "/orders/{name}" )
    public ResponseEntity deleteRecipe ( @PathVariable final String name ) {
        final CustomerOrder order = service.findByCustomerName( name );
        if ( null == order ) {
            return new ResponseEntity( errorResponse( "No order found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( order );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

    /**
     * REST API method to allow editing a Recipe from the CoffeeMaker's
     * Inventory, by making a PUT request to the API endpoint and indicating the
     * recipe to update (as a path variable)
     *
     * @param name
     *            The name of the Recipe to update
     * @param recipe
     *            The recipe to update with
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @PutMapping ( BASE_PATH + "/orders/fulfill/{name}" )
    public ResponseEntity fulfillOrder ( @PathVariable final String name ) {
        final CustomerOrder order = service.findByCustomerName( name );
        if ( null == order || !order.getStatus().equals( OrderStatus.PENDING ) ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + name ), HttpStatus.NOT_FOUND );
        }
        order.advanceStatus();
        service.save( order );

        return new ResponseEntity( successResponse( order.getCustomerName() + " was edited successfully" ),
                HttpStatus.OK );
    }

    /**
     * REST API method to allow editing a Recipe from the CoffeeMaker's
     * Inventory, by making a PUT request to the API endpoint and indicating the
     * recipe to update (as a path variable)
     *
     * @param name
     *            The name of the Recipe to update
     * @param recipe
     *            The recipe to update with
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @PutMapping ( BASE_PATH + "/orders/pickup/{name}" )
    public ResponseEntity pickupOrder ( @PathVariable final String name ) {
        final CustomerOrder order = service.findByCustomerName( name );
        if ( null == order || !order.getStatus().equals( OrderStatus.FULFILLED ) ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + name ), HttpStatus.NOT_FOUND );
        }
        order.advanceStatus();
        service.save( order );

        return new ResponseEntity( successResponse( order.getCustomerName() + " was edited successfully" ),
                HttpStatus.OK );
    }

}
