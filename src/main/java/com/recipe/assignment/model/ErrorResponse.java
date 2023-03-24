package com.recipe.assignment.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ErrorResponse implements IResponse{
    String message;    
}
