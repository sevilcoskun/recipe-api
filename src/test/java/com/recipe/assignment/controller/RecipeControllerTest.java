package com.recipe.assignment.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.recipe.assignment.model.Recipe;
import com.recipe.assignment.model.RequestBodyDto;
import com.recipe.assignment.model.SuccessResponse;
import com.recipe.assignment.model.ErrorResponse;
import com.recipe.assignment.model.IResponse;
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
    public void throw_error_when_get_all_recipes() {
        doThrow(MockitoException.class).when(recipeServiceMock).getAllRecipes();

        ResponseEntity<List<Recipe>> response = recipeControllerMock.getAllRecipes();

        assertThat(response).isNotNull();
        assertEquals(500, response.getStatusCode().value());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void get_recipe_by_id_successfully() {
        List<String> ingredients1 = List.of("a", "b", "c");
        Recipe recipe1 = new Recipe(1L, "name1", 4, ingredients1, true, "");

        Optional<Recipe> recipeOptional = Optional.of(recipe1);

        doReturn(recipeOptional).when(recipeServiceMock).getRecipeById(anyLong());

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setRecipe(recipeOptional);;
        ResponseEntity<IResponse> response = recipeControllerMock.getRecipeById(1L);

        assertThat(response).isNotNull();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(successResponse, response.getBody());
    }

    @Test
    public void fail_get_recipe_by_id() {
        Optional<Recipe> empty = Optional.empty();
        doReturn(empty).when(recipeServiceMock).getRecipeById(anyLong());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Given [1] Recipe is not Found");
        ResponseEntity<IResponse> response = recipeControllerMock.getRecipeById(1L);

        assertThat(response).isNotNull();
        assertEquals(404, response.getStatusCode().value());
        assertEquals(errorResponse, response.getBody());
    }

    @Test
    public void throw_error_when_get_recipe_by_id() {
        doThrow(MockitoException.class).when(recipeServiceMock).getRecipeById(anyLong());
        ResponseEntity<IResponse> response = recipeControllerMock.getRecipeById(1L);

        assertThat(response).isNotNull();
        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    public void add_recipe_successfully() {
        List<String> ingredients = List.of("pasta", "sauce", "water");
        Recipe recipe = new Recipe((Long) 1L, "pasta", 4, ingredients, true, "nothing");

        doNothing().when(recipeServiceMock).addRecipe(any());

        ResponseEntity<String> response = recipeControllerMock.addRecipe(recipe);

        assertThat(response).isNotNull();
        assertEquals(202, response.getStatusCode().value());
        assertTrue(response.getBody().equals("Recipe is created!"));
    }

    @Test
    public void fail_add_recipe() {
        List<String> ingredients = List.of("pasta", "sauce", "water");
        Recipe recipe = new Recipe((Long) 1L, "pasta", 0, ingredients, true, "nothing");

        doThrow(IllegalArgumentException.class).when(recipeServiceMock).addRecipe(any());

        ResponseEntity<String> response = recipeControllerMock.addRecipe(recipe);

        assertThat(response).isNotNull();
        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().equals("Given recipe name is already exists! Please try another one!"));
    }

    @Test
    public void update_recipe_successfully() {
        Recipe updatedRecipe = new Recipe(1L, "name2", 1, new ArrayList<>(), true, "asdasd");
        doNothing().when(recipeServiceMock).updateRecipe(anyLong(), any());
        ResponseEntity<IResponse> response = recipeControllerMock.updateRecipe(1L, updatedRecipe);

        assertThat(response).isNotNull();
        assertEquals(202, response.getStatusCode().value());
        assertTrue(response.getBody().getMessage().equals("Recipe [1] is updated"));
    }

    @Test
    public void fail_recipe_update() {
        Recipe updatedRecipe = new Recipe(1L, "name2", 1, new ArrayList<>(), true, "asdasd");
        doThrow(IllegalArgumentException.class).when(recipeServiceMock).updateRecipe(anyLong(), any());
        ResponseEntity<IResponse> response = recipeControllerMock.updateRecipe(1L, updatedRecipe);

        assertThat(response).isNotNull();
        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().getMessage().equals("Error - Recipe cannot be updated!"));
    }

    @Test
    public void delete_recipe_successfully() throws NotFoundException {
        doNothing().when(recipeServiceMock).deleteRecipe(anyLong());
        ResponseEntity<IResponse> response  = recipeControllerMock.deleteRecipe(5L);

        assertThat(response).isNotNull();
        assertEquals(202, response.getStatusCode().value());
        assertTrue(response.getBody().getMessage().equals("Recipe is deleted!"));
    }

    @Test
    public void throw_error_delete_recipe() throws NotFoundException {
        doThrow(NotFoundException.class).when(recipeServiceMock).deleteRecipe(anyLong());

        ResponseEntity<IResponse> response  = recipeControllerMock.deleteRecipe(5L);

        assertThat(response).isNotNull();
        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().getMessage().equals("Error - Given Recipe id [5] is not found to be deleted!")); 
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
        doReturn(empty).when(recipeServiceMock).search(any());

        RequestBodyDto request = new RequestBodyDto();

        ResponseEntity<List<Recipe>> response = recipeControllerMock.searchRecipe(request);

        assertThat(response).isNotNull();
        assertEquals(404, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }
}
