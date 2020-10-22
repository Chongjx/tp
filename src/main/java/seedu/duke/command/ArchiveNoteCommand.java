package seedu.duke.command;

import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.Notebook;
import seedu.duke.ui.Formatter;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static seedu.duke.util.PrefixSyntax.PREFIX_DELIMITER;
import static seedu.duke.util.PrefixSyntax.PREFIX_INDEX;
import static seedu.duke.util.PrefixSyntax.PREFIX_TITLE;
import static seedu.duke.util.PrefixSyntax.SUFFIX_INDEX;

/**
 * Archives a Note from the Notebook.
 */
public class ArchiveNoteCommand extends Command {

    public static final String COMMAND_WORD = "archive-n";

    public static final String COMMAND_USAGE = COMMAND_WORD + ": Archives a note. Parameters: "
            + "[" + PREFIX_DELIMITER + PREFIX_TITLE + " TITLE] "
            + "[" + PREFIX_DELIMITER + PREFIX_INDEX + " INDEX]";

    public static final String ARCHIVE_NOTE_MESSAGE = "The following note has been archived: ";
    public static final String COMMAND_UNSUCCESSFUL_MESSAGE = "This note does not exist in the notebook! ";

    private int index;
    private String title = "";

    /**
     * Constructs a ArchiveNoteCommand to archive a Note.
     *
     * @param index of the item to be archived.
     */
    public ArchiveNoteCommand(int index) {
        this.index = index;
    }

    /**
     * Constructs a ArchiveNoteCommand to archive a Note.
     *
     * @param title of the item to be deleted.
     */
    public ArchiveNoteCommand(String title) {
        this.title = title;
    }

    @Override
    public String execute() {

        try {
            // If there is no title, archive note by index. Else archive by title.
            if (title.isBlank()) {
                title = notebook.archiveNotes(index);
            } else {
                if (!notebook.archiveNotes(title)) {
                    return Formatter.formatString(COMMAND_UNSUCCESSFUL_MESSAGE);
                }
            }
            return Formatter.formatString(ARCHIVE_NOTE_MESSAGE + title);
        } catch (IndexOutOfBoundsException exception) {
            return Formatter.formatString(COMMAND_UNSUCCESSFUL_MESSAGE);
        }
    }
}
