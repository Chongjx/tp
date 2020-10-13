package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.TagManager;
import seedu.duke.data.notebook.Tag;
import seedu.duke.ui.InterfaceManager;
import static seedu.duke.command.DeleteTagCommand.COMMAND_SUCCESSFUL_MESSAGE;
import static seedu.duke.command.DeleteTagCommand.COMMAND_UNSUCCESSFUL_MESSAGE;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteTagCommandTest {

    private Tag tagRed;
    private Tag tagGreen;
    private Tag tagBlue;
    private Tag tagRedRef;
    private Tag tagGreenRef;

    private ArrayList<Tag> tags;
    private TagManager tagManager;

    private Note defaultNote;

    @BeforeEach
    void setUp() {
        tagRed = new Tag("Red", Tag.COLOR_RED_STRING);
        tagGreen = new Tag("Green", Tag.COLOR_GREEN_STRING);
        tagBlue  = new Tag("Blue", Tag.COLOR_BLUE_STRING);
        tagRedRef = new Tag("Red", Tag.COLOR_RED_STRING);
        tagGreenRef = new Tag("Green", Tag.COLOR_GREEN_STRING);

        tags = new ArrayList<>();
        tagManager = new TagManager();

        defaultNote = new Note("Default", "Default", false);
    }

    @Test
    void deleteTagCommand_deleteNonExistingTag_returnDefaultMessage() {
        tags.add(tagRed);
        tags.add(tagGreen);
        tags.add(tagBlue);

        String result = getCommandExecutionString(tagManager, tags);

        assertEquals(COMMAND_UNSUCCESSFUL_MESSAGE + tagRed + InterfaceManager.LS
                        + COMMAND_UNSUCCESSFUL_MESSAGE + tagGreen + InterfaceManager.LS
                        + COMMAND_UNSUCCESSFUL_MESSAGE + tagBlue
                , result);
    }

    @Test
    void deleteTagCommand_deleteExistingTag_deletesTag() {
        tagManager.createTag(tagRed, true);
        tagManager.createTag(tagGreen, true);

        tags.add(tagRed);
        tags.add(tagGreen);

        defaultNote.setTags(tags);
        tagManager.rebindTags(defaultNote);

        tags = new ArrayList<>();
        tags.add(tagRedRef);
        tags.add(tagGreenRef);

        // Ensures the note is tagged properly
        assertEquals(defaultNote.getTags().size(), 2);
        assertTrue(defaultNote.getTags().contains(tagRed));
        assertTrue(defaultNote.getTags().contains(tagGreen));
        assertEquals(tagManager.getTagMap().size(), 2);
        assertTrue(tagManager.getTagMap().get(tagRed).contains(defaultNote));
        assertTrue(tagManager.getTagMap().get(tagGreen).contains(defaultNote));

        String result = getCommandExecutionString(tagManager, tags);

        assertEquals(COMMAND_SUCCESSFUL_MESSAGE + tagRed + InterfaceManager.LS
                        + COMMAND_SUCCESSFUL_MESSAGE + tagGreen
                , result);

        assertEquals(defaultNote.getTags().size(), 0);
        assertEquals(tagManager.getTagMap().size(), 0);
    }

    @Test
    void deleteTagCommand_deleteNonExistingAndExistingTag_deletesTagAndReturnDefaultMessage() {
        tagManager.createTag(tagRed, true);
        tagManager.createTag(tagGreen, true);

        tags.add(tagRed);
        tags.add(tagGreen);

        defaultNote.setTags(tags);
        tagManager.rebindTags(defaultNote);

        tags = new ArrayList<>();
        tags.add(tagRedRef);
        tags.add(tagGreenRef);
        tags.add(tagBlue);

        // Ensures the note is tagged properly
        assertEquals(defaultNote.getTags().size(), 2);
        assertTrue(defaultNote.getTags().contains(tagRed));
        assertTrue(defaultNote.getTags().contains(tagGreen));
        assertEquals(tagManager.getTagMap().size(), 2);
        assertTrue(tagManager.getTagMap().get(tagRed).contains(defaultNote));
        assertTrue(tagManager.getTagMap().get(tagGreen).contains(defaultNote));

        String result = getCommandExecutionString(tagManager, tags);

        assertEquals(COMMAND_SUCCESSFUL_MESSAGE + tagRed + InterfaceManager.LS
                        + COMMAND_SUCCESSFUL_MESSAGE + tagGreen + InterfaceManager.LS
                + COMMAND_UNSUCCESSFUL_MESSAGE + tagBlue
                , result);

        assertEquals(defaultNote.getTags().size(), 0);
        assertEquals(tagManager.getTagMap().size(), 0);


    }

    private String getCommandExecutionString(TagManager tagManger, ArrayList<Tag> tags) {
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(tags);
        deleteTagCommand.setData(null, null, tagManger, null);
        return deleteTagCommand.execute();
    }
}
