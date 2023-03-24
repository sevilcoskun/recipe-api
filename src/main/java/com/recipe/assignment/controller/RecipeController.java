package com.recipe.assignment.controller;

import java.util.ArrayList;
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
import com.recipe.assignment.model.SuccessResponse;
import com.recipe.assignment.model.ErrorResponse;
import com.recipe.assignment.model.IResponse;
import com.recipe.assignment.service.RecipeService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Operation(summary = "Get all recipes")
    @GetMapping("/recipes")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        try {
            recipes = recipeService.getAllRecipes();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(recipes);
        }
        return ResponseEntity.status(HttpStatus.OK).body(recipes);
    }

    @Operation(summary = "Get a recipe by id")
    @GetMapping("/recipes/{id}")
    public ResponseEntity<IResponse> getRecipeById(@PathVariable Long id) {
        Optional<Recipe> recipe;
        ErrorResponse errorResponse = new ErrorResponse();
        try {
            recipe = recipeService.getRecipeById(id);
        } catch (Exception e) {
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        if (recipe.isPresent()) {
            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setRecipe(recipeService.getRecipeById(id));
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } else {
            errorResponse.setMessage("Given [" + id + "] Recipe is not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Create a recipe")
    @PostMapping("/recipes")
    public ResponseEntity<String> addRecipe(@RequestBody Recipe recipe) {
        try {
            recipeService.addRecipe(recipe);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Recipe cannot be created!" );
        }
        return ResponseEntity.accepted().body("Recipe is created!");

    }

    @Operation(summary = "Update a recipe by id")
    @PutMapping("/recipes/{id}")
    public ResponseEntity<IResponse> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        try {
            recipeService.updateRecipe(id, recipe);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Error - Recipe cannot be updated!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage("Recipe [" + id + "] is updated");
        return ResponseEntity.accepted().body(successResponse);
    }

    @Operation(summary = "Delete a recipe by id")
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<IResponse> deleteRecipe(@PathVariable Long id) {
        try {
            recipeService.deleteRecipe(id);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Error - Recipe cannot be deleted!");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage("Recipe is deleted!");
        return ResponseEntity.accepted().body(successResponse);
    }

    @Operation(summary = "Search a recipe by given conditions")
    @GetMapping("/recipes/search")
    public ResponseEntity<List<Recipe>> searchRecipe(@Validated @RequestBody RequestBodyDto request) {
        List<Recipe> recipe = recipeService.search(request);
        if (recipe.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(recipe);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
