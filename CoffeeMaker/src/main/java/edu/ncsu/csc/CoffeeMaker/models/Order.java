package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;

/**
 * Class for making and storing orders
 */
@Entity
public class Order extends DomainObject {

    /**
     * id for Order
     */
    @Id
    @GeneratedValue
    private Long         id;

    /**
     * customer name for Order
     */
    private final String customerName;

    /**
     * status of Order
     */
    private OrderStatus  status;

    /**
     * orderID for Order
     */
    private Integer      orderID;

    /**
     * review for Order
     */
    private String       review;

    /** placement time */
    private Long         placementTime;

    /** payment */
    @Min ( 0 )
    private Integer      payment;

    /**
     * Recipe
     */
    @OneToOne
    private Recipe       recipe;

    /**
     * returns recipe
     *
     * @return Recipe
     */
    public Recipe getRecipe () {
        return recipe;
    }

    /**
     * sets the recipe
     *
     * @param recipe
     *            of order
     */
    public void setRecipe ( final Recipe recipe ) {
        this.recipe = recipe;
    }

    /**
     * constructor
     *
     * @param customerName
     *            customers name
     */
    public Order ( final String customerName ) {
        this.customerName = customerName;

    }

    /**
     * advances the order to the next stage
     */
    public void advanceOrder () {
        if ( status.equals( OrderStatus.PENDING ) ) {
            status = OrderStatus.FULFILLED;
        }
        else if ( status.equals( OrderStatus.PENDING ) ) {
            status = OrderStatus.PICKEDUP;
        }

    }

    @Override
    public String toString () {
        final String rtn = customerName + ": ";

        // Use for each loop to go through every entry in map

        return rtn;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( customerName == null ) ? 0 : customerName.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Order other = (Order) obj;
        if ( customerName == null ) {
            if ( other.customerName != null ) {
                return false;
            }
        }
        else if ( !customerName.equals( other.customerName ) ) {
            return false;
        }
        return true;
    }

    @Override
    public Serializable getId () {

        return id;
    }

    /**
     * sets the id of order
     *
     * @param id
     *            id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * returns customer name
     *
     * @return customer name
     */
    public String getCustomerName () {
        return customerName;
    }

    /**
     * returns the current status
     *
     * @return status
     */
    public OrderStatus getStatus () {
        return status;
    }

    /**
     * returns OrderID
     *
     * @return Order ID
     */
    public Integer getOrderID () {
        return orderID;
    }

    /**
     * returns review
     *
     * @return review
     */
    public String getReview () {
        return review;
    }

    /**
     * sets the review
     *
     * @param review
     *            review to set
     */
    public void setReview ( final String review ) {
        this.review = review;
    }

    /**
     * returns placement time
     *
     * @return placement time
     */
    public Long getPlacementTime () {
        return placementTime;
    }

    /**
     * returns payment
     *
     * @return payment
     */
    public Integer getPayment () {
        return payment;
    }

}
