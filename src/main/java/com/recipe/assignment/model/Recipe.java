package com.recipe.assignment.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@Table(name = "recipes")
public class Recipe {

    @Id @GeneratedValue 
    private Long id;

    @Column(nullable = false, unique = true, name = "name")
    private String name;
    
    @Column(nullable = false)
    private int numberOfServing;
    
    @ElementCollection
    @Column(nullable = false)
    private List<String> ingredients;

    @Column(nullable = false)
    private boolean isVegaterian;
    
    @Column(nullable = false)
    private String instructions;
    
    // Constructor
    public Recipe(Long id, String name, int numberOfServing, List<String> ingredients, boolean isVegaterian, String instructions) {
        this.id = id;
        this.name = name;
        this.numberOfServing = numberOfServing;
        this.ingredients = ingredients;
        this.isVegaterian = isVegaterian;
        this.instructions = instructions;
    }
}
