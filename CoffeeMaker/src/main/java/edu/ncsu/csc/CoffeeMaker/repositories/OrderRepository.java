package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Order;

/**
 * Repository for orders
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * finds the order connected with a customers name
     *
     * @param name
     *            customers name
     * @return Order
     */
    Order findByCustomerName ( String name );

    /**
     * returns the order connected with the order id
     *
     * @param orderID
     *            order id
     * @return Order
     */
    Order findByOrderID ( Integer orderID );

}
