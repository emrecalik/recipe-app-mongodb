package com.edoras.recipeproject.converters;

import com.edoras.recipeproject.commands.NotesCommand;
import com.edoras.recipeproject.domains.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesCommandToNotesTest {

    NotesCommandToNotes notesCommandConverter;

    private final Long NOTES_ID = 1L;
    private final String NOTES_NOTES = "notes";

    @BeforeEach
    void setUp() {
        notesCommandConverter = new NotesCommandToNotes();
    }

    @Test
    void testNullSource() {
        assertNull(notesCommandConverter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(notesCommandConverter.convert(new NotesCommand()));
    }

    @Test
    void convert() {
        // given
        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(NOTES_ID);
        notesCommand.setRecipeNotes(NOTES_NOTES);

        // when
        Notes notes = notesCommandConverter.convert(notesCommand);

        // then
        assertEquals(NOTES_ID, notes.getId());
        assertEquals(NOTES_NOTES, notes.getRecipeNotes());
    }
}