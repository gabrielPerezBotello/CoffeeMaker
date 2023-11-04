package edu.ncsu.csc.CoffeeMaker.models;

/**
 * Order status Enumerations for Orders
 */
public enum OrderStatus {
    /**
     * For pending orders
     */
    PENDING,
    /**
     * For fulfilled orders
     */
    FULFILLED,
    /**
     * For orders that have been picked up
     */
    PICKEDUP
}
