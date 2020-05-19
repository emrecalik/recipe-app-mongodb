package com.edoras.recipeproject.domains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notes {

    private String id;
    private String recipeNotes;
    private Recipe recipe;
}
