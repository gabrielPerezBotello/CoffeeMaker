package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

/**
 * Class for making and storing orders
 */
@Entity
@Table ( name = "Orders" )
public class CustomerOrder extends DomainObject {

    /**
     * Represents the value of the review field BEFORE it is set to a valid
     * (i.e. non-empty) String.
     */
    private final String  REVIEW_NOT_SET  = null;

    /**
     * Represents the value of the payment field BEFORE it is set to a valid
     * (i.e. > 0) Integer.
     */
    private final Integer PAYMENT_NOT_SET = 0;

    /**
     * id for Order
     */
    @Id
    @GeneratedValue
    private Long          id;

    /**
     * customer name for Order
     */
    private final String  customerName;

    /**
     * status of Order
     */
    private OrderStatus   orderStatus;

    /**
     * orderID for Order
     */
    private final Integer orderID;

    /**
     * review for Order
     */
    private String        review;

    /** placement time */
    private final Long    placementTime;

    /** payment */
    @Min ( 0 )
    private Integer       payment;

    /**
     * Recipe
     */
    @OneToOne ( cascade = CascadeType.ALL )
    @JoinColumn ( nullable = false, name = "recipe_id" )
    private final Recipe  recipe;

    /**
     * Constructor
     *
     * @param customerName
     *            customers name
     * @param orderID
     *            ID of order
     * @param placementTime
     *            time order was placed
     */
    public CustomerOrder ( final String customerName, final Integer orderID, final Recipe recipe ) {
        this.customerName = customerName;
        this.orderStatus = OrderStatus.PENDING;
        this.orderID = orderID;
        this.review = this.REVIEW_NOT_SET;
        this.placementTime = System.currentTimeMillis();
        this.payment = this.PAYMENT_NOT_SET;
        this.recipe = recipe;

    }

    /**
     * advances the order to the next stage
     */
    public void advanceStatus () {
        if ( orderStatus.equals( OrderStatus.PENDING ) ) {
            orderStatus = OrderStatus.FULFILLED;
        }
        else if ( orderStatus.equals( OrderStatus.FULFILLED ) ) {
            orderStatus = OrderStatus.PICKEDUP;
        }

    }

    /**
     * returns the current status
     *
     * @return status
     */
    public OrderStatus getStatus () {
        return orderStatus;
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

        // Check whether the review has already been set.
        //
        if ( this.REVIEW_NOT_SET != this.getReview() ) { // begin if.

            // The review has already been set,
            // and therefore we don't change it.
            return;

        } // end if.

        // The review has not been set.

        // Check whether the passed-in review is
        // invalid (i.e. null, "").
        //
        if ( null == review || "".equals( review ) ) { // begin if.

            // The passed-in review is invalid,
            // and therefore we do not update
            // the review field.
            return;

        } // end if.

        // The passed-in review is valid.

        // The review has not been set,
        // and the passed-in review is valid,
        // therefore we update the review.
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
    /*
     * public void setRecipe ( final Recipe recipe ) { this.recipe = recipe; }
     */

    /**
     * returns payment
     *
     * @return payment
     */
    public Integer getPayment () {
        return payment;
    }

    /**
     * sets payment
     *
     * @param payment
     *            of order
     */
    public void setPayment ( final Integer payment ) {

        // Check whether the payment has already been made/set.
        //
        if ( this.PAYMENT_NOT_SET != this.getPayment() ) { // begin if.

            // The payment has already been set,
            // and therefore we don't change it.
            return;

        } // end if.

        // The payment has not been set.

        // Check whether the passed-in payment is
        // invalid (i.e. null, 0).
        //
        if ( null == payment || 0 == payment ) { // begin if.

            // The passed-in payment is invalid,
            // and therefore we do not update
            // the payment field.
            return;

        } // end if.

        // The passed-in payment is valid.

        // The payment has not been set,
        // and the passed-in payment is valid,
        // therefore we update the payment.
        this.payment = payment;
    }

    @Override
    public Serializable getId () {

        return id;
    }

    /**
     * returns a string descriptor of the object
     */
    @Override
    public String toString () {
        final String rtn = customerName + ": ";

        return rtn;
    }

    /**
     * generates a hash code for the Order
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( customerName == null ) ? 0 : customerName.hashCode() );
        return result;
    }

    /**
     * checks if 2 orders are equal
     */
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
        final CustomerOrder other = (CustomerOrder) obj;
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
}
