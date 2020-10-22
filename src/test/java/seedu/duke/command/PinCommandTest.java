package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.Notebook;

class PinCommandTest {
    private Note notePinned;
    private Note noteNotPinned;

    private Notebook notebook;

    private static final String NOTE_CONTENT = "This is a test note.";
    private static final String NOTE1_TITLE = "TestNote1";
    private static final String NOTE2_TITLE = "TestNote2";

    @BeforeEach
    void setUp() {
        notePinned = new Note(NOTE1_TITLE, NOTE_CONTENT, true, false);
        noteNotPinned = new Note(NOTE2_TITLE, NOTE_CONTENT, false, false);

        notebook = new Notebook();

        notebook.addNote(notePinned);
        notebook.addNote(noteNotPinned);
    }

    @Test
    void execute() {
    }

    @Test
    void execute_inputIndex_NoteExists_PinsAndUnpinsNotes() {
        //assertEquals(NOTE1_TITLE + " pinned: " + 'N', getExecutionStringInputIndex(notebook, 0));
        //assertEquals(NOTE2_TITLE + " pinned: " + 'Y', getExecutionStringInputIndex(notebook, 1));

        //assertTrue(notePinned.getPinned().equals("N"));
        //assertTrue(noteNotPinned.getPinned().equals("Y"));
    }

    @Test
    void execute_inputTitle_NoteExists_PinsAndUnpinsNotes() {
        //assertEquals(NOTE1_TITLE + " pinned: " + 'N', getExecutionStringInputTitle(notebook, NOTE1_TITLE));
        //assertEquals(NOTE2_TITLE + " pinned: " + 'Y', getExecutionStringInputTitle(notebook, NOTE2_TITLE));

        //assertTrue(notePinned.getPinned().equals("N"));
        //assertTrue(noteNotPinned.getPinned().equals("Y"));
    }

    @Test
    void execute_inputIndex_NoteDoesNotExists_returnsUnsuccessful() {
        assertEquals(PinCommand.COMMAND_UNSUCCESSFUL_MESSAGE, getExecutionStringInputIndex(notebook, 5));
        assertEquals(PinCommand.COMMAND_UNSUCCESSFUL_MESSAGE, getExecutionStringInputIndex(notebook, 3));
    }

    @Test
    void execute_inputTitle_NoteDoesNotExists_returnsUnsuccessful() {
        assertEquals(PinCommand.COMMAND_UNSUCCESSFUL_MESSAGE, getExecutionStringInputTitle(notebook, "Title"));
        assertEquals(PinCommand.COMMAND_UNSUCCESSFUL_MESSAGE, getExecutionStringInputTitle(notebook, "Random"));
    }

    private String getExecutionStringInputIndex(Notebook notebook, int index) {
        PinCommand pinCommand = new PinCommand(index);
        pinCommand.setData(notebook, null, null, null);

        return pinCommand.execute();
    }

    private String getExecutionStringInputTitle(Notebook notebook, String title) {
        PinCommand pinCommand = new PinCommand(title);
        pinCommand.setData(notebook, null,null, null);

        return pinCommand.execute();
    }
}
