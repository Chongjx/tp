package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.Notebook;
import seedu.duke.data.notebook.Tag;
import seedu.duke.ui.Formatter;

import java.util.ArrayList;

class ViewNoteCommandTest {
    private Note noteWithoutTags;
    private Note noteWithTags;

    private Notebook notebook;

    private static final ArrayList<String> NOTE1_CONTENT = new ArrayList<>();

    private static final ArrayList<String> NOTE2_CONTENT = new ArrayList<>();

    private static final String NOTE1_TITLE = "TestNote1";
    private static final String NOTE2_TITLE = "TestNote2";
    private Tag tag1 = new Tag("tag1", "PURPLE");
    private Tag tag2 = new Tag("tag2", "RED");

    private ArrayList<Tag> tags = new ArrayList<>();

    @BeforeEach
    void setUp() {
        NOTE1_CONTENT.add("This is a test note");

        NOTE2_CONTENT.add("This is a test note");
        NOTE2_CONTENT.add("With some lines");
        NOTE2_CONTENT.add("");
        NOTE2_CONTENT.add("This is the end");

        tags.add(tag1);
        tags.add(tag2);

        noteWithoutTags = new Note(NOTE1_TITLE, NOTE1_CONTENT, true, false);
        noteWithTags = new Note(NOTE2_TITLE, NOTE2_CONTENT, false, false, tags);

        notebook = new Notebook();

        notebook.addNote(noteWithoutTags);
        notebook.addNote(noteWithTags);
    }

    @Test
    void execute_inputIndex_NoteExists_returnsContent() {
        String tagsName = "";
        String contentOne = "";
        String contentTwo = "";

        for (Tag t : tags) {
            tagsName = tagsName.concat(t.toString());
        }

        for (String line : NOTE1_CONTENT) {
            contentOne += Formatter.LS + line;
        }
        for (String line : NOTE2_CONTENT) {
            contentTwo += Formatter.LS + line;
        }

        String note1ExpectedOutput = NOTE1_TITLE + " " + contentOne;
        String note2ExpectedOutput = NOTE2_TITLE + " " + tagsName + contentTwo;

        assertEquals(note1ExpectedOutput, getExecutionStringInputIndex(notebook, 0));
        assertEquals(note2ExpectedOutput, getExecutionStringInputIndex(notebook, 1));
    }

    @Test
    void execute_inputTitle_NoteExists_returnsContent() {
        String tagsName = "";
        String contentOne = "";
        String contentTwo = "";

        for (Tag t : tags) {
            tagsName = tagsName.concat(t.toString());
        }

        for (String line : NOTE1_CONTENT) {
            contentOne += Formatter.LS + line;
        }
        for (String line : NOTE2_CONTENT) {
            contentTwo += Formatter.LS + line;
        }

        String note1ExpectedOutput = NOTE1_TITLE + " " + contentOne;
        String note2ExpectedOutput = NOTE2_TITLE + " " + tagsName + contentTwo;

        assertEquals(note1ExpectedOutput, getExecutionStringInputTitle(notebook, NOTE1_TITLE));
        assertEquals(note2ExpectedOutput, getExecutionStringInputTitle(notebook, NOTE2_TITLE));
    }

    @Test
    void execute_inputIndex_NoteDoesNotExists_returnsUnsuccessful() {
        assertEquals(ViewNoteCommand.COMMAND_UNSUCCESSFUL_MESSAGE, getExecutionStringInputIndex(notebook, 5));
        assertEquals(ViewNoteCommand.COMMAND_UNSUCCESSFUL_MESSAGE, getExecutionStringInputIndex(notebook, 3));
    }

    @Test
    void execute_inputTitle_NoteDoesNotExists_returnsUnsuccessful() {
        assertEquals(ViewNoteCommand.COMMAND_UNSUCCESSFUL_MESSAGE, getExecutionStringInputTitle(notebook, "Title"));
        assertEquals(ViewNoteCommand.COMMAND_UNSUCCESSFUL_MESSAGE, getExecutionStringInputTitle(notebook, "Random"));
    }

    private String getExecutionStringInputIndex(Notebook notebook, int index) {
        ViewNoteCommand viewNoteCommand = new ViewNoteCommand(index);
        viewNoteCommand.setData(notebook, null, null, null);

        return viewNoteCommand.execute();
    }

    private String getExecutionStringInputTitle(Notebook notebook, String title) {
        ViewNoteCommand viewNoteCommand = new ViewNoteCommand(title);
        viewNoteCommand.setData(notebook, null, null, null);

        return viewNoteCommand.execute();
    }
}
