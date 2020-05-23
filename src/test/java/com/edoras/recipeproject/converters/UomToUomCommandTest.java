package com.edoras.recipeproject.converters;

import com.edoras.recipeproject.commands.UnitOfMeasureCommand;
import com.edoras.recipeproject.domains.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UomToUomCommandTest {

    UomToUomCommand uomConverter;

    private final String UOM_ID = "1";
    private final String UOM_UOM = "uom";

    @BeforeEach
    void setUp() {
        uomConverter = new UomToUomCommand();
    }

    @Test
    void testNullSource() {
        assertNull(uomConverter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(uomConverter.convert(new UnitOfMeasure()));
    }

    @Test
    void convert() {
        // given
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(UOM_ID);
        unitOfMeasure.setDescription(UOM_UOM);

        // when
        UnitOfMeasureCommand unitOfMeasureCommand = uomConverter.convert(unitOfMeasure);

        // then
        assertEquals(UOM_ID, unitOfMeasureCommand.getId());
        assertEquals(UOM_UOM, unitOfMeasureCommand.getUnitOfMeasure());
    }
}