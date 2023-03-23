package com.recipe.assignment.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.recipe.assignment.model.Recipe;
import com.recipe.assignment.model.RequestBodyDto;
import com.recipe.assignment.service.RecipeService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Operation(summary = "Get all recipes")
    @GetMapping("/recipes")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.status(HttpStatus.OK).body(recipeService.getAllRecipes());
    }

    @Operation(summary = "Get a recipe by id")
    @GetMapping("/recipes/{id}")
    public ResponseEntity<Optional<Recipe>> getRecipeById(@PathVariable Long id){
        if(recipeService.getRecipeById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(recipeService.getRecipeById(id));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Create a recipe")
    @PostMapping("/recipes")
    public ResponseEntity<String> addRecipe(@RequestBody Recipe recipe) {
        try {
            recipeService.addRecipe(recipe);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Recipe cannot be created!"); 
        }
        return ResponseEntity.ok().body("Recipe is created!");
        
    }

    @Operation(summary = "Update a recipe by id")
    @PutMapping("/recipes/{id}")
    public void updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        recipeService.updateRecipe(id, recipe);
    }

    @Operation(summary = "Delete a recipe by id")
    @DeleteMapping("/recipes/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }

    @Operation(summary = "Search a recipe by given conditions")
    @PostMapping("/recipes/search")
    public ResponseEntity<List<Recipe>> searchRecipe(@Validated @RequestBody RequestBodyDto request){
        List<Recipe> recipe = recipeService.search(request);
        if(recipe.size() > 0){
            return ResponseEntity.status(HttpStatus.OK).body(recipe);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
}
