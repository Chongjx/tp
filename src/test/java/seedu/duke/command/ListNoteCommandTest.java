package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.Notebook;
import seedu.duke.data.notebook.Tag;
import seedu.duke.ui.Formatter;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListNoteCommandTest {

    Notebook notebook;

    ArrayList<Note> noteArrayList = new ArrayList<>();

    Note defaultNote;
    Note testNote1;
    Note cs2113;
    Note songLyrics;

    @BeforeEach
    void setup() {
        notebook = new Notebook();

        Tag tagSports = new Tag("Sports", Tag.COLOR_RED_STRING);
        Tag tagCs2113 = new Tag("CEG", Tag.COLOR_YELLOW_STRING);
        Tag tagNus  = new Tag("NUS", Tag.COLOR_BLUE_STRING);

        ArrayList<Tag> tag = new ArrayList<>();
        ArrayList<Tag> tagSet = new ArrayList<>();

        tag.add(tagSports);
        tagSet.add(tagCs2113);
        tagSet.add(tagNus);

        defaultNote = new Note("Default", "default", false, false, tag);
        testNote1 = new Note("TestNote1", "testing", true, false);
        cs2113 = new Note("CS2113", "JavaDocs", true, false, tagSet);
        songLyrics = new Note("Song Lyrics", "I like to move it move it", false, false);

        //notebook.addNote(testNote1);
        //notebook.addNote(CS2113);
    }

    @Test
    void execute_noNotes_notebookIsEmpty() {
        //String expected = Formatter.LS
        //        + ListNoteCommand.COMMAND_UNSUCCESSFUL_MESSAGE_EMPTY_NOTEBOOK;

        //String actual = getCommandExecutionString(notebook);

        //assertEquals(expected, actual);
    }

    @Test
    void execute_noPinnedNotes_defaultList() {
        //notebook.addNote(defaultNote);
        //notebook.addNote(songLyrics);

        //String expected = Formatter.LS
                //+ ListNoteCommand.COMMAND_SUCCESSFUL_MESSAGE
                //+ "1. Default [91m[Sports][0m"
                //+ Formatter.LS
                //+ "2. Song Lyrics "
                //+ Formatter.LS;

        //String actual = getCommandExecutionString(notebook);

        //assertEquals(expected, actual);
    }

    private String getCommandExecutionString(Notebook notebook) {
        ListNoteCommand listNoteCommand = new ListNoteCommand();
        listNoteCommand.setData(notebook, null, null, null);
        return listNoteCommand.execute();
    }
}
