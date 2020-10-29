package seedu.notus.command;

import seedu.notus.data.exception.SystemException;
import seedu.notus.data.notebook.Note;
import seedu.notus.ui.Formatter;

import java.io.IOException;
import java.util.ArrayList;

import static seedu.notus.util.CommandMessage.ADD_NOTE_SUCCESSFUL_MESSAGE;
import static seedu.notus.util.CommandMessage.FILE_WRITE_UNSUCCESSFUL_MESSAGE;
import static seedu.notus.util.CommandMessage.NOTE_UNSUCCESSFUL_MESSAGE;
import static seedu.notus.util.parser.Parser.inputContent;

//@@author Nazryl
/**
 * Adds a Note into the Notebook.
 */
public class AddNoteCommand extends Command {

    public static final String COMMAND_WORD = "add-n";

    private Note note;

    /**
     * Constructs an AddNoteCommand to add a Note into the Notebook.
     *
     * @param note refers to the note to be added.
     */
    public AddNoteCommand(Note note) {
        this.note = note;
    }

    @Override
    public String execute() {
        ArrayList<String> content = note.getContent();

        // Search for duplicates
        if (notebook.getNote(note.getTitle())) {
            return Formatter.formatString(NOTE_UNSUCCESSFUL_MESSAGE);
        }

        // Get Content
        try {
            if (storageManager.noteExists(note, note.getIsArchived())) {
                content = storageManager.getNoteContent(note, note.getIsArchived());
            }
        } catch (SystemException exception) {
            return Formatter.formatString(exception.getMessage());
        }

        if (content.isEmpty()) {
            content = inputContent();
        }
        // Edit the note
        note.setContent(content);

        // Rebind the tags if there are duplicated tags
        tagManager.rebindTags(note);
        notebook.addNote(note);

        //Save the notes in storage
        try {
            storageManager.saveNote(note, note.getIsArchived());
        } catch (IOException exception) {
            return Formatter.formatString(FILE_WRITE_UNSUCCESSFUL_MESSAGE);
        }

        return Formatter.formatNote(ADD_NOTE_SUCCESSFUL_MESSAGE, note);
    }
}
