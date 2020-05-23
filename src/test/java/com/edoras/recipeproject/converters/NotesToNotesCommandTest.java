package com.edoras.recipeproject.converters;

import com.edoras.recipeproject.commands.NotesCommand;
import com.edoras.recipeproject.domains.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesToNotesCommandTest {

    NotesToNotesCommand notesConverter;

    private final String NOTES_ID = "1";
    private final String NOTES_NOTES = "notes";

    @BeforeEach
    void setUp() {
        notesConverter = new NotesToNotesCommand();
    }

    @Test
    void testNullSource() {
        assertNull(notesConverter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(notesConverter.convert(new Notes()));
    }

    @Test
    void convert() {
        // given
        Notes notes = new Notes();
        notes.setId(NOTES_ID);
        notes.setRecipeNotes(NOTES_NOTES);

        // when
        NotesCommand notesCommand = notesConverter.convert(notes);

        // then
        assertEquals(NOTES_ID, notesCommand.getId());
        assertEquals(NOTES_NOTES, notesCommand.getRecipeNotes());
    }
}