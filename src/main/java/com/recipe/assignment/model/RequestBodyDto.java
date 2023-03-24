package com.recipe.assignment.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RequestBodyDto {
    @Nullable
    public Boolean filterAllVegeterian; // true -> all vegeterian, false -> non-vegeterian
    int filterNumberOfServings;
    List<String> includedIngredients = new ArrayList<>();
    List<String> excludedIngredients = new ArrayList<>();
    String filterInstruction = "";
}
