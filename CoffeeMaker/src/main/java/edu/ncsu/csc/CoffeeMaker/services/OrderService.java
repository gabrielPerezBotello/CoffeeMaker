package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.repositories.OrderRepository;

/**
 * Service for orders
 */
@Component
@Transactional
public class OrderService extends Service<Order, Long> {

    /**
     * order repository
     */
    @Autowired
    private OrderRepository orderRepository;

    @Override
    protected JpaRepository<Order, Long> getRepository () {
        return orderRepository;
    }

    /**
     * finds the order connected with a customers name
     *
     * @param name
     *            customers name
     * @return Order
     */
    public Order findByCustomerName ( final String name ) {
        return orderRepository.findByCustomerName( name );
    }

    /**
     * returns the order connected with the order id
     *
     * @param orderID
     *            order id
     * @return Order
     */
    public Order findByOrderID ( final Integer orderID ) {
        return orderRepository.findByOrderID( orderID );
    }

    /**
     * returns all the orders in the repository
     *
     * @return List of Orders
     */
    public List<Order> getOrders () {
        return orderRepository.findAll();
    }
}
