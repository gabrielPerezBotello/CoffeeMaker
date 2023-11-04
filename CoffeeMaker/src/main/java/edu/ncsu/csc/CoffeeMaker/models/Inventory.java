package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** Current guest ID */
    private Integer          nextGuestID;

    /** Current order ID */
    private Integer          nextOrderID;

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long             id;

    /** Ingredients */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        this.ingredients = new ArrayList<Ingredient>();
        this.nextGuestID = 0;
        this.nextOrderID = 0;
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }

    /**
     * Create inventory with specified amounts
     *
     * @param ingredients
     *            the list of ingredients for inventory
     */
    public Inventory ( final List<Ingredient> ingredients ) {
        setIngredients( ingredients );
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * returns the ingredients in the inventory as a Map
     *
     * @return list of ingredients
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Sets the ingredients in the inventory
     *
     * @param ingredients
     *            ingredients to set
     */
    public void setIngredients ( final List<Ingredient> ingredients ) {
        this.ingredients = ingredients;
    }

    /**
     * Add the number of ingredient units in the inventory to the current amount
     * of ingredient units.
     *
     * @param name
     *            name of ingredient
     * @param amt
     *            amount of ingredient
     * @return checked amount of chocolate
     * @throws IllegalArgumentException
     *             if the parameter isn't a positive integer
     */
    public Integer checkIngredient ( final String name, final String amt ) throws IllegalArgumentException {
        Integer amount = 0;
        try {
            amount = Integer.parseInt( amt );
        }
        catch ( final NumberFormatException e ) {
            throw new IllegalArgumentException( "Units of " + name + " must be a positive integer" );
        }
        if ( amount < 0 ) {
            throw new IllegalArgumentException( "Units of " + name + " must be a positive integer" );
        }

        return amount;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        boolean isEnough = true;

        // Get list of recipe ingredients
        final List<Ingredient> tempIngredients = r.getIngredients();
        for ( final Ingredient i : tempIngredients ) {
            for ( final Ingredient j : ingredients ) {
                if ( i.getName().equals( j.getName() ) && j.getAmount() < i.getAmount() ) {
                    isEnough = false;
                }
            }
        }
        return isEnough;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        final List<Ingredient> tempIngredients = r.getIngredients();

        if ( enoughIngredients( r ) ) {
            for ( final Ingredient i : tempIngredients ) {
                for ( final Ingredient j : ingredients ) {
                    if ( i.getName().equals( j.getName() ) ) {
                        j.setAmount( j.getAmount() - i.getAmount() );
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredientList
     *            list of ingredient
     * @return true if successful, false if not
     */
    public boolean addIngredients ( final List<Ingredient> ingredientList ) {
        for ( final Ingredient i : ingredientList ) {
            if ( i.getAmount() < 0 ) {
                return false;
            }
        }

        for ( final Ingredient j : ingredientList ) {
            addIngredient( j );
        }

        return true;

    }

    /**
     * Adds an ingredient to the inventory
     *
     * @param ingredient
     *            to add
     */
    public void addIngredient ( final Ingredient ingredient ) {
        final Ingredient i = getIngredient( ingredient.getName() );

        if ( i == null ) {
            ingredients.add( ingredient );
        }
        else {
            i.setAmount( i.getAmount() + ingredient.getAmount() );
        }
    }

    /**
     * Gets the ingredient in the inventory based on the name
     *
     * @param name
     *            the name of ingredient to find
     * @return the found ingredient, otherwise null
     */
    public Ingredient getIngredient ( final String name ) {
        for ( final Ingredient i : ingredients ) {
            if ( i.getName().equals( name ) ) {
                return i;
            }
        }
        return null;
    }

    /**
     * Returns the current guest ID and increments it
     *
     * @return Current guest ID
     */
    public int getIncrNextGuestID () {
        final int val = nextGuestID++;
        return val;
    }

    /**
     * Returns the current order ID and increments it
     *
     * @return Current order ID
     */
    public int getIncrNextOrderID () {
        final int val = nextOrderID++;
        return val;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( final Ingredient entry : this.ingredients ) {
            buf.append( entry.getName() + ": " );
            buf.append( entry.getAmount() );
            buf.append( "\n" );
        }
        return buf.toString();
    }

}
