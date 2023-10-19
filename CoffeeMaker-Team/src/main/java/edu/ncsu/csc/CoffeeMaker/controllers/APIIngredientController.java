package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * API class for controlling API calls
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {

    /**
     * creates an Ingredient service for the controller class to use
     */
    @Autowired
    private IngredientService service;

    /**
     * returns all ingredients
     *
     * @return list of ingredients
     */
    @GetMapping ( BASE_PATH + "/ingredient" )
    public List<Ingredient> getIngredients () {
        return service.findAll();
    }

    /**
     * returns a specific ingredient
     *
     * @param name
     *            name of ingredient
     * @return Ingredient with the name provided
     */
    @GetMapping ( BASE_PATH + "ingredient/{name}" )
    public ResponseEntity getIngredient ( @PathVariable final String name ) {
        final List<Ingredient> list = service.findAll();
        Ingredient ing = null;
        for ( final Ingredient i : list ) {
            if ( i.getName().toUpperCase().equals( name.toUpperCase() ) ) {
                ing = i;
            }
        }
        if ( null == ing ) {
            return new ResponseEntity( errorResponse( "No ingredient found with name " + name ), HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity( ing, HttpStatus.OK );
    }

    /**
     * creates an ingredient
     *
     * @param ingredient
     *            ingredient passed through to save
     * @return response entity
     */
    @PostMapping ( BASE_PATH + "/ingredient" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingredient ) {
        final List<Ingredient> list = service.findAll();
        for ( final Ingredient i : list ) {
            if ( i.getName().equals( ingredient.getName() ) ) {
                return new ResponseEntity(
                        errorResponse( "Ingredient with the name " + ingredient.getName() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
        }

        service.save( ingredient );
        return new ResponseEntity( successResponse( ingredient.getName() + " successfully created" ), HttpStatus.OK );

    }

    /**
     * REST API method to allow deleting an Ingredient from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the ingredient to delete (as a path variable)
     *
     * @param name
     *            The name of the Ingredient to delete
     * @return Success if the ingredient could be deleted; an error if the
     *         ingredient does not exist
     */
    @DeleteMapping ( BASE_PATH + "/ingredient/{name}" )
    public ResponseEntity deleteIngredient ( @PathVariable final String name ) {
        final List<Ingredient> list = service.findAll();
        Ingredient ing = null;
        for ( final Ingredient i : list ) {
            if ( i.getName() == null ) {
                return new ResponseEntity( errorResponse( "No ingredient found for name " + name ),
                        HttpStatus.NOT_FOUND );
            }
            if ( i.getName().toUpperCase().equals( name.toUpperCase() ) ) {
                ing = i;
            }
        }
        if ( ing == null ) {
            return new ResponseEntity( errorResponse( "No ingredient found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( ing );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

}
