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
     * REST API method to allow deleting a Customer Order from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the Customer Order to delete (as a path variable)
     *
     * @param name
     *            The name of the Customer Order to delete
     * @return Success if the Customer Order could be deleted; an error if the
     *         Customer Order does not exist
     */
    @DeleteMapping ( BASE_PATH + "/orders/{name}" )
    public ResponseEntity deleteCustomerOrder ( @PathVariable final String name ) {
        final CustomerOrder order = service.findByCustomerName( name );
        if ( null == order ) {
            return new ResponseEntity( errorResponse( "No order found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( order );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

    /**
     * REST API method to allow editing a Customer Order from the CoffeeMaker's
     * Inventory, by making a PUT request to the API endpoint and indicating the
     * Customer Order to update (as a path variable)
     *
     * @param orderID
     *            The orderID of the Customer Order to update
     *
     * @return Success if the Customer Order could be deleted; an error if the
     *         Customer Order does not exist
     */
    @PutMapping ( BASE_PATH + "/orders/fulfill/{orderID}" )
    public ResponseEntity fulfillOrder ( @PathVariable final Integer orderID ) {
        final CustomerOrder order = service.findByOrderID( orderID );
        if ( null == order || !order.getStatus().equals( OrderStatus.PENDING ) ) {
            return new ResponseEntity( errorResponse( "No order found for order ID: " + orderID ),
                    HttpStatus.NOT_FOUND );
        }
        order.advanceStatus();
        service.save( order );

        return new ResponseEntity( successResponse( order.getOrderID() + " was edited successfully" ), HttpStatus.OK );
    }

    /**
     * REST API method to allow editing a Customer Order from the CoffeeMaker's
     * Inventory, by making a PUT request to the API endpoint and indicating the
     * Customer Order to update (as a path variable)
     *
     * @param orderID
     *            The orderID of the Customer Order to update
     * @return Success if the Customer Order could be deleted; an error if the
     *         Customer Order does not exist
     */
    @PutMapping ( BASE_PATH + "/orders/pickup/{orderID}" )
    public ResponseEntity pickupOrder ( @PathVariable final Integer orderID ) {
        final CustomerOrder order = service.findByOrderID( orderID );
        if ( null == order || !order.getStatus().equals( OrderStatus.FULFILLED ) ) {
            return new ResponseEntity( errorResponse( "No Order found for name " + orderID ), HttpStatus.NOT_FOUND );
        }
        order.advanceStatus();
        service.save( order );

        return new ResponseEntity( successResponse( order.getOrderID() + " was edited successfully" ), HttpStatus.OK );
    }

}
