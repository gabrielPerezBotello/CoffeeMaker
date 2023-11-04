package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.repositories.OrderRepository;

@Component
@Transactional
public class OrderService extends Service<Order, Long> {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    protected JpaRepository<Order, Long> getRepository () {
        return orderRepository;
    }

    public Order findByCustomerName ( final String name ) {
        return orderRepository.findByCustomerName( name );
    }

    public Order findByOrderID ( final Integer orderID ) {
        return orderRepository.findByOrderID( orderID );
    }

    public List<Order> getOrders () {
        return orderRepository.findAll();
    }
}
