package com.edoras.recipeproject.converters;

import com.edoras.recipeproject.commands.IngredientCommand;
import com.edoras.recipeproject.domains.Ingredient;
import com.edoras.recipeproject.domains.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientToIngredientCommandTest {

    IngredientToIngredientCommand ingredientConverter;

    private final long INGREDIENT_ID = 1L;
    private final String INGREDIENT_DESCRIPTION = "ingredient description";
    private final BigDecimal INGREDIENT_AMOUNT = new BigDecimal(1);
    private final long UOM_ID = 2L;

    @BeforeEach
    void setUp() {
        ingredientConverter = new IngredientToIngredientCommand(new UomToUomCommand());
    }

    @Test
    void testNullSource() {
        assertNull(ingredientConverter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(ingredientConverter.convert(new Ingredient()));
    }

    @Test
    void convert() {
        // given
        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);
        ingredient.setDescription(INGREDIENT_DESCRIPTION);
        ingredient.setAmount(INGREDIENT_AMOUNT);

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(UOM_ID);

        ingredient.setUnitOfMeasure(unitOfMeasure);

        // when
        IngredientCommand ingredientCommand = ingredientConverter.convert(ingredient);

        // then
        assertEquals(INGREDIENT_ID, ingredientCommand.getId());
        assertEquals(INGREDIENT_DESCRIPTION, ingredientCommand.getDescription());
        assertEquals(INGREDIENT_AMOUNT, ingredientCommand.getAmount());
        assertEquals(UOM_ID, ingredientCommand.getUnitOfMeasureCommand().getId());
    }
}