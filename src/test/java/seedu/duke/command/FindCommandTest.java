package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;

import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.Notebook;

import org.junit.jupiter.api.Test;
import seedu.duke.ui.Formatter;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FindCommandTest {

    Notebook notebook;

    @BeforeEach
    void setup() {
        notebook = new Notebook();
        ArrayList<String> content = new ArrayList<>();
        content.add("default");

        Note defaultNote = new Note("Default", content, false, false);
        Note testNote1 = new Note("TestNote1", content, false, false);
        Note testNote2 = new Note("TestNote2", content, false, false);

        notebook.addNote(defaultNote);
        notebook.addNote(testNote1);
        notebook.addNote(testNote2);
    }

    @Test
    void execute_keywordTest_returnsTestNote1AndTestNote2() {
        String keyword = "Test";

        String expected = "Here are the matching notes in your list:"
                + Formatter.LS
                + "1. TestNote1 "
                + Formatter.LS
                + "2. TestNote2 "
                + Formatter.LS;
        String result = getCommandExecutionString(notebook, keyword);

        assertEquals(expected, result);

    }

    @Test
    void execute_keywordDef_returnsDefault() {
        String keyword = "def";

        String expected = "Here are the matching notes in your list:"
                + Formatter.LS
                + "1. Default "
                + Formatter.LS;
        String result = getCommandExecutionString(notebook, keyword);

        assertEquals(expected, result);
    }

    @Test
    void execute_keywordNil_returnsNoMatch() {
        String keyword = "NIL";

        String expected = "There are no matching notes. "
                + "Please try another search query.";
        String result = getCommandExecutionString(notebook, keyword);

        assertEquals(expected, result);
    }

    private String getCommandExecutionString(Notebook notebook, String keyword) {
        FindCommand findCommand = new FindCommand(keyword);
        findCommand.setData(notebook, null, null, null);
        return findCommand.execute();
    }
}
