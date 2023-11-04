package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByName ( String name );

    Order findByID ( Integer orderID );

    List<Order> getOrders ();
}
