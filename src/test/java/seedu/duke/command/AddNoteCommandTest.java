package seedu.duke.command;

import org.junit.jupiter.api.Test;

import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.Notebook;
import seedu.duke.data.notebook.TagManager;
import seedu.duke.data.timetable.Timetable;

import seedu.duke.storage.StorageManager;
import seedu.duke.ui.Formatter;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AddNoteCommandTest {

    /**
     * Asserts that the note can be added successfully.
     */
    @Test
    public void addCommand_emptyNotebook_notebookContainsPerson() {
        Notebook notebook = new Notebook();
        Timetable timetable = new Timetable();
        TagManager tagManager = new TagManager();
        StorageManager storageManager = new StorageManager();

        Note note = new Note("Java OOP", "Encapsulation\nAbstraction", true, false);
        AddNoteCommand command = null;
        try {
            command = new AddNoteCommand(note);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        command.setData(notebook, timetable, tagManager, storageManager);
        String result = command.execute();
        ArrayList<Note> checkBook = notebook.getNotes();

        assertTrue(checkBook.contains(note));
        assertEquals(1, checkBook.size());
        assertFalse(result.equals(Formatter.formatString(AddNoteCommand.COMMAND_UNSUCCESSFUL_MESSAGE)));
        assertEquals(Formatter.formatNote(AddNoteCommand.COMMAND_SUCCESSFUL_MESSAGE + note.getTitle(), note), result);
    }

    /**
     * Asserts that the note cannot be added when the note
     * contains the same title.
     */
    @Test
    public void addCommand_notebookAlreadyContainsTitle_notebookUnmodified() {
        Notebook notebook = new Notebook();
        Timetable timetable = new Timetable();
        TagManager tagManager = new TagManager();
        StorageManager storageManager = new StorageManager();

        Note note = new Note("Java OOP", "Encapsulation\nAbstraction", true, false);
        notebook.addNote(note);
        AddNoteCommand command = null;
        try {
            command = new AddNoteCommand(note);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        command.setData(notebook, timetable, tagManager, storageManager);
        String result = command.execute();

        assertFalse(result.contains(note.getTitle()));
        assertEquals(Formatter.formatString(AddNoteCommand.COMMAND_UNSUCCESSFUL_MESSAGE), result);
        ArrayList<Note> checkBook = notebook.getNotes();
        assertTrue(checkBook.contains(note));
        assertEquals(1, checkBook.size());
    }
}
