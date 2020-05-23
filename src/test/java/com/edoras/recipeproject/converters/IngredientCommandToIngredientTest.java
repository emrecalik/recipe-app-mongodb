package com.edoras.recipeproject.converters;

import com.edoras.recipeproject.commands.IngredientCommand;
import com.edoras.recipeproject.commands.UnitOfMeasureCommand;
import com.edoras.recipeproject.domains.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientCommandToIngredientTest {

    IngredientCommandToIngredient ingredientCommandConverter;

    private final String INGREDIENT_ID = "1";
    private final String INGREDIENT_DESCRIPTION = "ingredient description";
    private final BigDecimal INGREDIENT_AMOUNT = new BigDecimal(1);
    private final String UOM_ID = "2";

    @BeforeEach
    void setUp() {
        ingredientCommandConverter = new IngredientCommandToIngredient(new UomCommandToUom());
    }

    @Test
    void testNullSource() {
        assertNull(ingredientCommandConverter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(ingredientCommandConverter.convert(new IngredientCommand()));
    }

    @Test
    void convert() {
        // given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setDescription(INGREDIENT_DESCRIPTION);
        ingredientCommand.setAmount(INGREDIENT_AMOUNT);

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(UOM_ID);
        ingredientCommand.setUnitOfMeasureCommand(unitOfMeasureCommand);

        // when
        Ingredient ingredient = ingredientCommandConverter.convert(ingredientCommand);

        // then
        assertEquals(INGREDIENT_ID, ingredient.getId());
        assertEquals(INGREDIENT_DESCRIPTION, ingredient.getDescription());
        assertEquals(INGREDIENT_AMOUNT, ingredient.getAmount());
        assertEquals(UOM_ID, ingredient.getUnitOfMeasure().getId());
    }
}