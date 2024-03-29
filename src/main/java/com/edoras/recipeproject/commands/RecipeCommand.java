package com.edoras.recipeproject.commands;

import com.edoras.recipeproject.domains.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {
    private String id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String description;

    @Min(1)
    @Max(999)
    private int prepTime;

    @Min(1)
    @Max(999)
    private int cookTime;

    @Min(1)
    @Max(100)
    private int servings;

    private String source;

    @URL
    private String url;

    @NotBlank
    private String directions;

    private NotesCommand notesCommand;
    private List<IngredientCommand> ingredientCommands = new ArrayList<>();
    private List<CategoryCommand> categoryCommands = new ArrayList<>();
    private Difficulty difficulty;
    private Byte[] image;
}
