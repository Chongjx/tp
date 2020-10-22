package seedu.notus.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.notus.data.notebook.Note;
import seedu.notus.data.notebook.Notebook;
import seedu.notus.data.notebook.TagManager;
import seedu.notus.data.notebook.Tag;
import seedu.notus.ui.Formatter;

import static seedu.notus.command.TagCommand.COMMAND_UNSUCCESSFUL_MESSAGE;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TagCommandTest {

    private Tag tagRed;
    private Tag tagBlue;

    private Tag tagRedRef;
    private Tag tagBlueRef;

    private Note noTagNote;
    private Note taggedNote;

    private ArrayList<Tag> tags;
    private TagManager tagManager;
    private Notebook notebook;

    @BeforeEach
    void setUp() {
        tagRed = new Tag("Red", Tag.COLOR_RED_STRING);
        tagBlue = new Tag("Blue", Tag.COLOR_BLUE_STRING);
        tagRedRef = new Tag("Red", Tag.COLOR_RED_STRING);
        tagBlueRef = new Tag("Blue", Tag.COLOR_BLUE_STRING);

        noTagNote = new Note("Default", "default", false, false);
        taggedNote = new Note("TaggedNote", "default", false, false);

        tags = new ArrayList<>();

        tagManager = new TagManager();
        notebook = new Notebook();
    }

    @Test
    void tagCommand_invalidIndex_returnsUnsuccessfulMessage() {
        notebook.addNote(noTagNote);
        notebook.addNote(taggedNote);

        tags = new ArrayList<>();

        String result = getCommandExecutionString(notebook, tagManager, 3, tags);
        assertEquals(Formatter.formatString(COMMAND_UNSUCCESSFUL_MESSAGE), result);
    }

    @Test
    void tagCommand_tagNote_tagsNote() {
        tags.add(tagRed);
        tags.add(tagBlue);

        taggedNote.setTags(tags);
        tagManager.rebindTags(taggedNote);

        notebook.addNote(noTagNote);
        notebook.addNote(taggedNote);

        getCommandExecutionString(notebook, tagManager, 0, tags);

        assertEquals(noTagNote.getTags().size(), 2);
        assertTrue(noTagNote.getTags().contains(tagRed));
        assertEquals(tagManager.getTagMap().size(), 2);
        assertEquals(tagManager.getTagMap().get(tagRed).size(), 2);
    }

    @Test
    void tagCommand_untagNote_untagsNote() {
        tags.add(tagRed);
        tags.add(tagBlue);

        taggedNote.setTags(tags);
        tagManager.rebindTags(taggedNote);

        notebook.addNote(noTagNote);
        notebook.addNote(taggedNote);

        tags = new ArrayList<>();
        tags.add(tagRedRef);
        tags.add(tagBlueRef);

        getCommandExecutionString(notebook, tagManager, 1, tags);

        assertEquals(taggedNote.getTags().size(), 0);
        assertEquals(tagManager.getTagMap().get(tagRed).size(), 0);
        assertEquals(tagManager.getTagMap().get(tagBlue).size(), 0);
    }

    @Test
    void tagCommand_tagAndUntagNote_tagsNoteAndUntagsNote() {
        tags.add(tagRed);
        tags.add(tagBlue);

        taggedNote.setTags(tags);
        tagManager.rebindTags(taggedNote);

        tags = new ArrayList<>();
        tags.add(tagBlue);

        noTagNote.setTags(tags);

        notebook.addNote(noTagNote);
        notebook.addNote(taggedNote);

        tags = new ArrayList<>();
        tags.add(tagRedRef);
        tags.add(tagBlueRef);

        getCommandExecutionString(notebook, tagManager, 0, tags);

        assertEquals(noTagNote.getTags().size(), 1);
        assertEquals(tagManager.getTagMap().get(tagRed).size(), 2);
        assertEquals(tagManager.getTagMap().get(tagBlue).size(), 1);
    }

    private String getCommandExecutionString(Notebook notebook, TagManager tagManager, int index, ArrayList<Tag> tags) {
        TagCommand tagCommand = new TagCommand(index, tags);
        tagCommand.setData(notebook, null, tagManager, null);
        return tagCommand.execute();
    }
}