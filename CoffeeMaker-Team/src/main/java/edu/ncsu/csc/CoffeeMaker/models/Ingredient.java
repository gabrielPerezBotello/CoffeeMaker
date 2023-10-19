package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Ingredient extends DomainObject {

    /** Ingredient id */
    @Id
    @GeneratedValue
    private Long    id;

    /** Ingredient name */
    private String  name;

    /** Ingredient amount */
    @Min ( 0 )
    private Integer amount;

    /**
     * Creates an ingredient of type itype and amount amt
     *
     * @param itype
     *            type
     * @param amt
     *            amount
     */
    public Ingredient ( final String itype, final Integer amt ) {
        this.name = itype;
        this.amount = amt;
    }

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Ingredient () {
    }

    /**
     * Gets ingredient id
     *
     * @return id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets ID
     *
     * @param id
     *            id to set
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Gets ingredient type
     *
     * @return ingredient
     */
    public String getName () {
        return name;
    }

    /**
     * Sets ingredient type
     *
     * @param ingredient
     *            ingredient name
     */
    public void setName ( final String ingredient ) {
        this.name = ingredient;
    }

    /**
     * Gets ingredient amount
     *
     * @return amount
     */
    public Integer getAmount () {
        return amount;
    }

    /**
     * Sets ingredient amount
     *
     * @param amount
     *            amount of ingredient to put in the inventory
     */
    public void setAmount ( final Integer amount ) {
        this.amount = amount;
    }

    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + name + ", amount=" + amount + "]";
    }

}
