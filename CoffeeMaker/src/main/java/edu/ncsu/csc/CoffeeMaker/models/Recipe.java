package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long            id;

    /** Recipe name */
    private String          name;

    /** Recipe price */
    @Min ( 0 )
    private Integer         price;

    /** Ingredients */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    public List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Used to add abstract ingredients to the recipe
     *
     * @param i
     *            ingredient to add to the recipe
     */
    public void addIngredient ( final Ingredient i ) {
        ingredients.add( i );
    }

    /**
     * returns a map of ingredients in the recipe
     *
     * @return ingredient map
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * returns a map of ingredients in the recipe
     *
     * @param ing
     *            ingredients to set
     */
    public void setIngredients ( final List<Ingredient> ing ) {
        this.ingredients = ing;
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {
        for ( final Ingredient i : ingredients ) {
            if ( i.getAmount() != 0 ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Gets ingredient based on name
     *
     * @param name
     *            the name of ingredient to retrieve
     * @return the needed ingredient
     * @throws IllegalArgumentException
     *             if name of ingredient isnt found
     */
    public Ingredient getIngredient ( final String name ) {
        for ( final Ingredient i : ingredients ) {
            if ( i.getName().equals( name ) ) {
                return i;
            }
        }

        throw new IllegalArgumentException( "Ingredient could not be found" );
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        String rtn = name + ": ";

        // Use for each loop to go through every entry in map
        for ( final Ingredient entry : ingredients ) {
            final String newName = entry.getName();
            final Ingredient ingredient = entry;
            rtn += newName + "=" + ingredient.getAmount() + " ";
        }

        return rtn;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
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
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
