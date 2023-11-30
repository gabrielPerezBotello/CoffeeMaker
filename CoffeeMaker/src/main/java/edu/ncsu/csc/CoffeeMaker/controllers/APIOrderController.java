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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.OrderStatus;
import edu.ncsu.csc.CoffeeMaker.services.CustomerOrderService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Customer Orders.
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
     * CustomerOrderService object, to be autowired in by Spring to allow for
     * manipulating the Customer Order model
     */
    @Autowired
    private CustomerOrderService service;
    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService     iService;

    /**
     * REST API method to provide GET access to all Customer Orders in the
     * system
     *
     * @return JSON representation of all Customer Orders
     */
    @GetMapping ( BASE_PATH + "/orders" )
    public List<CustomerOrder> getOrders () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific Orders, as indicated
     * by the path variable provided (the name of the customer who ordered
     * desired)
     *
     * @param name
     *            Customer Order name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/orders/{name}" )
    public List<CustomerOrder> getOrdersCustomer ( @PathVariable ( "name" ) final String name ) { // begin
                                                                                                  // func().
        final List<CustomerOrder> orders = service.findAll();
        final List<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>();
        for ( int i = 0; i < orders.size(); i++ ) { // begin for.

            final CustomerOrder order = orders.get( i );

            if ( order.getCustomerName().equals( name ) ) { // begin if.
                if ( OrderStatus.PENDING == order.getStatus() ) { // begin if.

                    customerOrders.add( order );

                } // end if.

                if ( OrderStatus.FULFILLED == order.getStatus()
                        && System.currentTimeMillis() - order.getPlacementTime() < 300000 ) { // begin
                                                                                              // if.

                    customerOrders.add( order );

                } // end if.

            } // end if.

        } // end for.

        return customerOrders;

    } // end func().

    /**
     * REST API method to provide POST access to the Customer model. This is
     * used to create a new Customer Order by automatically converting the JSON
     * RequestBody provided to a Customer Order object. Invalid JSON will fail.
     *
     * @param order
     *            The valid Order to be saved.
     * @return ResponseEntity indicating success if the Customer Order could be
     *         saved to the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/orders" )
    public ResponseEntity placeOrder ( @RequestBody final CustomerOrder order ) {
        System.out.println( order.getCustomerName() );
        System.out.println( order.getId() );
        System.out.println( order.getPayment() );

        // final CustomerOrder newOrder = new CustomerOrder( username, 0,
        // (Recipe) recipeService.findById( id ) );
        final double change = order.getPayment() - order.getRecipe().getPrice();
        if ( change < 0 ) {
            return new ResponseEntity( errorResponse( "Not enough money paid" ), HttpStatus.CONFLICT );
        }
        service.save( order );

        return new ResponseEntity<String>( successResponse( String.valueOf( change ) ), HttpStatus.OK );

    }

    /**
     * REST API method to allow deleting a Customer Order from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the Customer Order to delete (as a path variable)
     *
     * @param id
     *            The id of the Customer Order to delete
     * @return Success if the Customer Order could be deleted; an error if the
     *         Customer Order does not exist
     */
    @DeleteMapping ( BASE_PATH + "/orders/{id}" )
    public ResponseEntity deleteCustomerOrder ( @PathVariable final Long id ) {
        final CustomerOrder order = service.findById( id );
        if ( null == order ) {
            return new ResponseEntity( errorResponse( "No order found for Id: " + id ), HttpStatus.NOT_FOUND );
        }
        service.delete( order );

        return new ResponseEntity( successResponse( id + " was deleted successfully" ), HttpStatus.OK );
    }

    /**
     * REST API method to allow editing a Customer Order from the CoffeeMaker's
     * Inventory, by making a PUT request to the API endpoint and indicating the
     * Customer Order to update (as a path variable)
     *
     * @param id
     *            The id of the Customer Order to update
     *
     * @return Success if the Customer Order could be deleted; an error if the
     *         Customer Order does not exist
     */
    @PutMapping ( BASE_PATH + "/orders/fulfill/{id}" )
    public ResponseEntity fulfillOrder ( @PathVariable final Long id ) {
        final CustomerOrder order = service.findById( id );
        if ( null == order || !order.getStatus().equals( OrderStatus.PENDING ) ) {
            return new ResponseEntity( errorResponse( "No order found for order ID: " + id ), HttpStatus.NOT_FOUND );
        }

        final Inventory inventoryCurrent = iService.getInventory(); // gets
                                                                    // current
                                                                    // inventory
                                                                    // object
        inventoryCurrent.useIngredients( order.getRecipe() );
        iService.save( inventoryCurrent );
        order.advanceStatus();
        service.save( order );

        return new ResponseEntity( successResponse( order.getId() + " was edited successfully" ), HttpStatus.OK );
    }

    /**
     * REST API method to allow editing a Customer Order from the CoffeeMaker's
     * Inventory, by making a PUT request to the API endpoint and indicating the
     * Customer Order to update (as a path variable)
     *
     * @param id
     *            The id of the Customer Order to update
     * @param review
     *            The Customer's review for the Order
     * @return Success if the Customer Order could be deleted; an error if the
     *         Customer Order does not exist
     */
    @PutMapping ( BASE_PATH + "/orders/pickup/{id}" )
    public ResponseEntity pickupOrder ( @PathVariable final Long id, @RequestBody final String review ) {
        final CustomerOrder order = service.findById( id );
        if ( null == order || !order.getStatus().equals( OrderStatus.FULFILLED ) ) {
            return new ResponseEntity( errorResponse( "No Order found for name " + id ), HttpStatus.NOT_FOUND );
        }
        order.advanceStatus();
        order.setReview( review );
        service.save( order );

        return new ResponseEntity( successResponse( order.getId() + " was edited successfully" ), HttpStatus.OK );
    }

}
