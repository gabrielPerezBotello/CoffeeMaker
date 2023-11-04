package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByCustomerName ( String name );

    Order findByOrderID ( Integer orderID );

}
