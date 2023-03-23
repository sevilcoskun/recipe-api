package com.recipe.assignment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recipe.assignment.model.Recipe;
import com.recipe.assignment.model.RequestBodyDto;
import com.recipe.assignment.repository.RecipeRepository;

@Service
public class RecipeService {
    
    @Autowired
    private RecipeRepository repository;

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        recipes = (List<Recipe>) repository.findAll();
        return recipes;
    }

    public Optional<Recipe> getRecipeById(Long id) {
       Optional<Recipe> recipe = repository.findById(id);
       return recipe;
    }

    public void addRecipe(Recipe recipe) {
        repository.save(recipe);
    }

    public void updateRecipe(Long id, Recipe updatedRecipe) {
        Optional<Recipe> recipe = repository.findById(id);
        if(recipe.isPresent()){
            updatedRecipe.setId(recipe.get().getId());
            repository.save(updatedRecipe);
        }
        else throw new IllegalArgumentException();
        
    }

    public void deleteRecipe(Long id) {
        repository.deleteById(id);
    }

    public List<Recipe> search(RequestBodyDto request){        
        List<Recipe> recipes = (List<Recipe>) repository.findAll();
        List<Recipe> result = new ArrayList<>();

        if(request.isFilterAllVegeterian()){
            List<Recipe> allVegeretianRecipesList = recipes.stream().filter(r -> r.isVegaterian()).collect(Collectors.toList());
            if(result.size() == 0){
                result.addAll(allVegeretianRecipesList);
            }
        }

        if(request.isFilterAllNonVegeterian()){
            List<Recipe> allNonVegeretianRecipestList = recipes.stream().filter(r -> !r.isVegaterian()).collect(Collectors.toList());
            if(result.size() == 0) {
                result.addAll(allNonVegeretianRecipestList);
            }
            else {
                result.retainAll(allNonVegeretianRecipestList);
            }
        }

        if(request.getFilterNumberOfServings() > 0){
            List<Recipe> filteredNumberOfServingsList = recipes.stream().filter(r -> r.getNumberOfServing() == request.getFilterNumberOfServings()).collect(Collectors.toList());
            if(result.size() == 0) {
                result.addAll(filteredNumberOfServingsList);
            }
            else {
                result.retainAll(filteredNumberOfServingsList);
            }
        }

        if(request.getIncludedIngredients().size() > 0){
            List<String> ingredients = request.getIncludedIngredients();
            List<Recipe> includedIngredientsList = recipes.stream().filter(r -> r.getIngredients().containsAll(ingredients)).collect(Collectors.toList());
            if(result.size() == 0) {
                result.addAll(includedIngredientsList);
            }
            else {
                result.retainAll(includedIngredientsList);
            }
        }

        if(request.getExcludedIngredients().size() > 0){
            List<String> ingredients = request.getExcludedIngredients();
            List<Recipe> excludedIngredientsList = recipes.stream().filter(r -> !r.getIngredients().containsAll(ingredients)).collect(Collectors.toList());
            if(result.size() == 0) {
                result.addAll(excludedIngredientsList);
            }
            else {
                result.retainAll(excludedIngredientsList);
            }
        }

        if(request.getFilterInstruction().length() > 0){
            List<Recipe> filteredInstuctionsList = recipes.stream().filter(r -> r.getInstructions().contains(request.getFilterInstruction())).collect(Collectors.toList());
            if(result.size() == 0) {
                result.addAll(filteredInstuctionsList);
            }
            else {
                result.retainAll(filteredInstuctionsList);
            }
        }

        return result;
        
    }
}
