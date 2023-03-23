package com.recipe.assignment.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.recipe.assignment.model.Recipe;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

}
