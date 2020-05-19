package com.edoras.recipeproject.converters;

import com.edoras.recipeproject.commands.UnitOfMeasureCommand;
import com.edoras.recipeproject.domains.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UomCommandToUomTest {

    UomCommandToUom uomCommandConverter;
    private final Long UOM_ID = 1L;
    private final String UOM_UOM = "uom";

    @BeforeEach
    void setUp() {
        uomCommandConverter = new UomCommandToUom();
    }

    @Test
    void testNullSource() {
        uomCommandConverter.convert(null);
    }

    @Test
    void testEmptySource() {
        uomCommandConverter.convert(new UnitOfMeasureCommand());
    }

    @Test
    void convert() {
        // given
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(UOM_ID);
        unitOfMeasureCommand.setUnitOfMeasure(UOM_UOM);

        // when
        UnitOfMeasure unitOfMeasure = uomCommandConverter.convert(unitOfMeasureCommand);

        // then
        assertEquals(UOM_ID, unitOfMeasure.getId());
        assertEquals(UOM_UOM, unitOfMeasure.getDescription());
    }
}