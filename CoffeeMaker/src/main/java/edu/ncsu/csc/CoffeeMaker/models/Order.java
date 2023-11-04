package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;

@Entity
public class Order extends DomainObject {

    @Id
    @GeneratedValue
    private Long         id;

    private final String customerName;

    private OrderStatus  status;

    private Integer      orderID;

    private String       review;

    /** Recipe name */
    private Long         placementTime;

    /** Recipe price */
    @Min ( 0 )
    private Integer      payment;

    @OneToOne
    private Recipe       recipe;

    public Recipe getRecipe () {
        return recipe;
    }

    public void setRecipe ( final Recipe recipe ) {
        this.recipe = recipe;
    }

    public Order ( final String customerName ) {
        this.customerName = customerName;

    }

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

    public void setId ( final Long id ) {
        this.id = id;
    }

    public String getCustomerName () {
        return customerName;
    }

    public OrderStatus getStatus () {
        return status;
    }

    public Integer getOrderID () {
        return orderID;
    }

    public String getReview () {
        return review;
    }

    public void setReview ( final String review ) {
        this.review = review;
    }

    public Long getPlacementTime () {
        return placementTime;
    }

    public Integer getPayment () {
        return payment;
    }

}
