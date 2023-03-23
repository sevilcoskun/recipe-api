package com.recipe.assignment.model;

import java.util.Optional;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SuccessResponse implements IResponse{
    Optional<Recipe> recipe;
}
