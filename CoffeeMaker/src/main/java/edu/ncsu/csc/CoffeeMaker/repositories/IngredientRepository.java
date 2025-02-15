package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

/**
 * Ingredients repository
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
