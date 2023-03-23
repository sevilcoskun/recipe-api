package com.recipe.assignment.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.recipe.assignment.model.Recipe;
import com.recipe.assignment.model.RequestBodyDto;
import com.recipe.assignment.repository.RecipeRepository;
import com.recipe.assignment.service.RecipeService;

@ExtendWith(SpringExtension.class)
public class RecipeControllerTest {

    @Mock
    private RecipeService recipeServiceMock;

    @Mock
    private RecipeRepository recipeRepositoryMock;

    @InjectMocks
    private RecipeController recipeControllerMock;

    @Before(value = "")
    public void setUp() {
        recipeServiceMock = new RecipeService();
    }

    @Test
    public void get_all_recipes_successfully() {
        List<String> ingredients1 = List.of("a", "b", "c");
        Recipe recipe1 = new Recipe(1L, "name1", 4, ingredients1, true, "");
        List<String> ingredients2 = List.of("a", "s", "d");
        Recipe recipe2 = new Recipe(2L, "name2", 3, ingredients2, false, "");
        List<Recipe> recipes = List.of(recipe1, recipe2);

        doReturn(recipes).when(recipeServiceMock).getAllRecipes();

        ResponseEntity<List<Recipe>> response = recipeControllerMock.getAllRecipes();

        assertThat(response).isNotNull();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void get_recipe_by_id_successfully() {
        List<String> ingredients1 = List.of("a", "b", "c");
        Recipe recipe1 = new Recipe(1L, "name1", 4, ingredients1, true, "");

        Optional<Recipe> recipeOptional = Optional.of(recipe1);

        doReturn(recipeOptional).when(recipeServiceMock).getRecipeById(anyLong());

        ResponseEntity<Optional<Recipe>> response = recipeControllerMock.getRecipeById(1L);

        assertThat(response).isNotNull();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(recipeOptional, response.getBody());
    }

    @Test
    public void fail_get_recipe_by_id() {
        Optional<Recipe> empty = Optional.empty();
        doReturn(empty).when(recipeServiceMock).getRecipeById(anyLong());

        ResponseEntity<Optional<Recipe>> response = recipeControllerMock.getRecipeById(1L);

        assertThat(response).isNotNull();
        assertEquals(404, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    public void add_recipe_successfully() {
        List<String> ingredients = List.of("pasta", "sauce", "water");
        Recipe recipe = new Recipe((Long) 1L, "pasta", 4, ingredients, true, "nothing");

        doNothing().when(recipeServiceMock).addRecipe(any());
        recipeServiceMock.addRecipe(recipe);

        ResponseEntity<String> response = recipeControllerMock.addRecipe(recipe);

        assertThat(response).isNotNull();
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().equals("Recipe is created!"));
    }

    @Test
    public void update_recipe_successfully() {
        Recipe updatedRecipe = new Recipe(1L, "name2", 1, new ArrayList<>(), true, "asdasd");
        doNothing().when(recipeServiceMock).updateRecipe(anyLong(), any());
        recipeControllerMock.updateRecipe(1L, updatedRecipe);
    }

    @Test
    public void delete_recipe_successfully() {
        doNothing().when(recipeServiceMock).deleteRecipe(anyLong());
        recipeControllerMock.deleteRecipe(5L);
    }

    @Test
    public void search_recipe_successfully() {
        List<String> ingredients = List.of("pasta", "sauce", "water");
        Recipe recipe = new Recipe((Long) 1L, "pasta", 4, ingredients, true, "nothing");
        List<Recipe> recipes = List.of(recipe);

        RequestBodyDto request = new RequestBodyDto();

        doReturn(recipes).when(recipeServiceMock).search(any());
        ResponseEntity<List<Recipe>> response = recipeControllerMock.searchRecipe(request);

        assertThat(response).isNotNull();
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().equals(recipes));

    }

    @Test
    public void fail_search_recipe() {
        List<Recipe> empty = new ArrayList<>();
        doReturn(empty).when(recipeServiceMock).getRecipeById(anyLong());

        RequestBodyDto request = new RequestBodyDto();

        ResponseEntity<List<Recipe>> response = recipeControllerMock.searchRecipe(request);

        assertThat(response).isNotNull();
        assertEquals(404, response.getStatusCode().value());
        assertTrue(response.getBody().equals(null));
    }
}
