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
    ArrayList<Tag> tag = new ArrayList<>();
    ArrayList<Tag> tagSet = new ArrayList<>();

    Note defaultNote;
    Note testNote1;
    Note cs2113;
    Note songLyrics;

    Tag tagSports;
    Tag tagCs2113;
    Tag tagNus;

    @BeforeEach
    void setup() {
        notebook = new Notebook();

        tagSports = new Tag("Sports", Tag.COLOR_RED_STRING);
        tagCs2113 = new Tag("CEG", Tag.COLOR_YELLOW_STRING);
        tagNus = new Tag("NUS", Tag.COLOR_BLUE_STRING);

        ArrayList<String> content = new ArrayList<>();
        content.add("default");
        ArrayList<String> contentOne = new ArrayList<>();
        contentOne.add("testing");
        ArrayList<String> contentTwo = new ArrayList<>();
        contentTwo.add("JavaDocs");
        ArrayList<String> contentThree = new ArrayList<>();
        contentThree.add("I like to move it move it");

        tag.add(tagSports);
        tagSet.add(tagCs2113);
        tagSet.add(tagNus);

        defaultNote = new Note("Default", content, false, false, tag);
        testNote1 = new Note("TestNote1", contentOne, true, false);
        cs2113 = new Note("CS2113", contentTwo, true, false, tagSet);
        songLyrics = new Note("Song Lyrics", contentThree, false, false);

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
