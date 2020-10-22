package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.Notebook;
import seedu.duke.data.notebook.TagManager;
import seedu.duke.data.timetable.Timetable;

import seedu.duke.storage.StorageManager;
import seedu.duke.ui.Formatter;

import java.util.ArrayList;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteNoteCommandTest {
    private Notebook deletedNotebook;
    private Notebook expectedNotebook;
    private Notebook notebook = new Notebook();
    private Timetable timetable = new Timetable();
    private TagManager tagManager = new TagManager();
    private StorageManager storageManager = new StorageManager();

    @BeforeEach
    public void setUp() {
        ArrayList<String> contentOne = new ArrayList<>();
        contentOne.add("Encapsulation");
        contentOne.add("Abstraction");

        ArrayList<String> contentTwo = new ArrayList<>();
        contentTwo.add("Core library");
        contentTwo.add("Standard library");

        Note javaNote = new Note("Java OOP", contentOne, true, false);
        Note cppNote = new Note("C++ Standard Libraries", contentTwo, false, false);

        AddNoteCommand command = null;
        try {
            command = new AddNoteCommand(javaNote);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }

        command.setData(notebook, timetable, tagManager, storageManager);
        command.execute();
        deletedNotebook = notebook;

        try {
            command = new AddNoteCommand(cppNote);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        command.setData(notebook, timetable, tagManager, storageManager);
        command.execute();

        expectedNotebook = notebook;
    }

    /**
     * Delete notes by index and String title of the note.
     */
    @Test
    public void execute_validIndex_personIsDeleted() {
        assertDeletionSuccessfulInteger(1, notebook);
        assertDeletionSuccessfulString("Java OOP", notebook);
    }

    /**
     * Asserts that the note at the specified index can be deleted.
     * The Notebook passed in will not be modified.
     *
     * @param targetVisibleIndex of the item to be deleted.
     * @param notebook           contains list of notes.
     */
    private void assertDeletionSuccessfulInteger(int targetVisibleIndex, Notebook notebook) {

        String deleteTarget = notebook.getNotes().get(targetVisibleIndex).getTitle();
        String expectedMessage = DeleteNoteCommand.COMMAND_SUCCESSFUL_MESSAGE + deleteTarget;

        DeleteNoteCommand command = createDeleteCommand(targetVisibleIndex, notebook);
        String result = command.execute();

        assertEquals(Formatter.formatString(expectedMessage), result);
        assertEquals(deletedNotebook.getNotes(), notebook.getNotes());
    }

    /**
     * Asserts that the note at can be deleted with the note title.
     * The Notebook passed in will not be modified.
     *
     * @param targetString of the item to be deleted.
     * @param notebook     contains list of notes.
     */
    private void assertDeletionSuccessfulString(String targetString, Notebook notebook) {

        ArrayList<Note> deletedListTitle = (ArrayList<Note>) notebook.getNotes().stream()
                .filter((s) -> s.getTitle().toLowerCase().contains(targetString.toLowerCase()))
                .collect(toList());
        String deleteTarget = deletedListTitle.get(0).getTitle();
        String expectedMessage = DeleteNoteCommand.COMMAND_SUCCESSFUL_MESSAGE + deleteTarget;

        DeleteNoteCommand command = createDeleteCommand(targetString, notebook);
        String result = command.execute();

        assertEquals(Formatter.formatString(expectedMessage), result);
        assertEquals(deletedNotebook.getNotes(), notebook.getNotes());
    }

    @Test
    public void execute_emptyAddressBook_returnsPersonNotFoundMessage() {
        assertDeletionFailsDueToNoSuchPersonInteger(2, notebook);
        assertDeletionFailsDueToNoSuchPersonString("ZZZ", notebook);
    }

    /**
     * Creates a new delete command using index to delete.
     *
     * @param targetVisibleIndex of the item to be deleted.
     * @param notebook           contains list of notes.
     */
    private DeleteNoteCommand createDeleteCommand(int targetVisibleIndex, Notebook notebook) {

        DeleteNoteCommand command = new DeleteNoteCommand(targetVisibleIndex);
        command.setData(notebook, timetable, tagManager, storageManager);
        return command;
    }

    /**
     * Creates a new delete command using String to delete.
     *
     * @param targetVisibleString of the item to be deleted.
     * @param notebook            contains list of notes.
     */
    private DeleteNoteCommand createDeleteCommand(String targetVisibleString, Notebook notebook) {
        DeleteNoteCommand command = new DeleteNoteCommand(targetVisibleString);
        command.setData(notebook, timetable, tagManager, storageManager);
        return command;
    }

    /**
     * Asserts that the note at the specified index cannot be deleted
     * due to note not existing in the notebook.
     * The Notebook passed in will not be modified.
     *
     * @param visibleIndex of the item to be deleted.
     * @param notebook     contains list of notes.
     */
    private void assertDeletionFailsDueToNoSuchPersonInteger(int visibleIndex, Notebook notebook) {
        DeleteNoteCommand command = createDeleteCommand(visibleIndex, notebook);
        String result = command.execute();

        assertEquals(Formatter.formatString(DeleteNoteCommand.COMMAND_UNSUCCESSFUL_MESSAGE), result);
        assertEquals(expectedNotebook.getNotes(), notebook.getNotes());
    }

    /**
     * Asserts that the note at the specified index cannot be deleted
     * due to note not existing in the notebook.
     * The Notebook passed in will not be modified.
     *
     * @param targetVisibleString of the item to be deleted.
     * @param notebook            contains list of notes.
     */
    private void assertDeletionFailsDueToNoSuchPersonString(String targetVisibleString, Notebook notebook) {
        DeleteNoteCommand command = createDeleteCommand(targetVisibleString, notebook);
        String result = command.execute();

        assertEquals(Formatter.formatString(DeleteNoteCommand.COMMAND_UNSUCCESSFUL_MESSAGE), result);
        assertEquals(expectedNotebook.getNotes(), notebook.getNotes());
    }
}
