package com.recipe.assignment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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
        if(recipe.getNumberOfServing() <= 0){
            throw new IllegalArgumentException("Number of Servings should be at least 1");
        }
        List<Recipe> recipes = (List<Recipe>) repository.findAll();
        for (Recipe r: recipes) {
           if(r.getName() == recipe.getName()){
            throw new IllegalArgumentException("Given recipe 'name' is already exists! Please try another one!");
           }
        }
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

    public void deleteRecipe(Long id) throws NotFoundException {
        Optional<Recipe> recipe = repository.findById(id);
        if(recipe.isPresent()){
            repository.deleteById(id);
        }
        else throw new NotFoundException();
    }

    public List<Recipe> search(RequestBodyDto request){        
        List<Recipe> recipes = (List<Recipe>) repository.findAll();
        List<Recipe> result = new ArrayList<>();

        if(request.filterAllVegeterian != null){
            if(request.getFilterAllVegeterian() == true){
                List<Recipe> allVegeretianRecipesList = recipes.stream().filter(r -> r.isVegaterian()).collect(Collectors.toList());
                result.addAll(allVegeretianRecipesList);
            }
            else{
                List<Recipe> allNonVegeretianRecipestList = recipes.stream().filter(r -> !r.isVegaterian()).collect(Collectors.toList());
                result.addAll(allNonVegeretianRecipestList);
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
