package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerOrderRepository;

/**
 * Service for orders
 */
@Component
@Transactional
public class CustomerOrderService extends Service<CustomerOrder, Long> {

    /**
     * order repository
     */
    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Override
    protected JpaRepository<CustomerOrder, Long> getRepository () {
        return customerOrderRepository;
    }

    /**
     * finds the order connected with a customers name
     *
     * @param name
     *            customers name
     * @return Order
     */
    public CustomerOrder findByCustomerName ( final String name ) {
        return customerOrderRepository.findByCustomerName( name );
    }

    /**
     * returns the order connected with the order id
     *
     * @param orderID
     *            order id
     * @return Order
     */
    public CustomerOrder findByOrderID ( final Integer orderID ) {
        return customerOrderRepository.findByOrderID( orderID );
    }

    /**
     * returns all the orders in the repository
     *
     * @return List of Orders
     */
    public List<CustomerOrder> getOrders () {
        return customerOrderRepository.findAll();
    }
}
