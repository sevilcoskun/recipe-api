package com.recipe.assignment.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RequestBodyDto {
    boolean filterAllVegeterian;
    boolean filterAllNonVegeterian;
    int filterNumberOfServings;
    List<String> includedIngredients = new ArrayList<>();
    List<String> excludedIngredients = new ArrayList<>();
    String filterInstruction = "";
}
