package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;

/**
 * Repository for orders
 */
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    /**
     * finds the order connected with a customers name
     *
     * @param name
     *            customers name
     * @return Order
     */
    CustomerOrder findByCustomerName ( String name );

}
