package seedu.duke.command;

import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.Tag;
import seedu.duke.ui.Formatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static seedu.duke.ui.Formatter.formatNotes;
import static seedu.duke.util.PrefixSyntax.PREFIX_DELIMITER;
import static seedu.duke.util.PrefixSyntax.PREFIX_TAG;

/**
 * Lists all the Notes in the Notebook.
 */
public class ListNoteCommand extends Command {

    public static final String COMMAND_WORD = "list-n";

    public static final String COMMAND_USAGE = COMMAND_WORD + ": Lists all the notes in the Notebook. Parameters: "
            + "[" + PREFIX_DELIMITER + PREFIX_TAG + " TAG "
            + PREFIX_DELIMITER + PREFIX_TAG + " TAG1...] "
            + "[/sort up OR down]";

    public static final String PINNED_MESSAGE = "Here are the list of pinned notes:";
    public static final String UNPINNED_MESSAGE = "Here are the list of unpinned notes:";
    public static final String COMMAND_SUCCESSFUL_MESSAGE = "Here are the list of notes:";
    public static final String COMMAND_UNSUCCESSFUL_MESSAGE_INVALID_TAG = "Your tags return no result."
            + " Please try an alternative tag or check your spellings";
    public static final String COMMAND_UNSUCCESSFUL_MESSAGE_EMPTY_NOTEBOOK = "The notebook is empty!";

    private ArrayList<String> tags;
    private boolean isSorted;
    private boolean isArchived;
    private Boolean isAscendingOrder;

    /**
     * Constructs a ListCommand to list all the Notes in the Notebook in a sorted order.
     *
     * @param isAscendingOrder determines the order of the sorting of the Notes.
     */
    public ListNoteCommand(Boolean isAscendingOrder) {
        this.tags = null;
        this.isSorted = true;
        this.isArchived = false;
        this.isAscendingOrder = isAscendingOrder;
    }

    /**
     * Constructs a ListNoteCommand to list all the Notes in the Notebook in the default order.
     */
    public ListNoteCommand() {
        this.tags = null;
        this.isSorted = false;
        this.isArchived = false;
        this.isAscendingOrder = null;
    }

    /**
     * Constructs a ListNoteCommand to list all the Notes in the Archived Notebook.
     */
    public ListNoteCommand(boolean isArchived) {
        this.tags = null;
        this.isSorted = false;
        this.isArchived = true;
        this.isAscendingOrder = null;
    }

    /**
     * Constructs a ListNoteCommand to list all the Notes in the Notebook that has the tag(s).
     *
     * @param tags tags of the Notes.
     */
    public ListNoteCommand(ArrayList<String> tags) {
        this.isSorted = false;
        this.isArchived = false;
        this.isAscendingOrder = null;
        this.tags = tags;
    }

    /**
     * Constructs a ListNoteCommand to list all the Notes in the Notebook, that has the tag(s), in a sorted order.
     *
     * @param isAscendingOrder order of the sort.
     * @param tags tags of the Notes.
     */
    public ListNoteCommand(Boolean isAscendingOrder, ArrayList<String> tags) {
        this.isAscendingOrder = isAscendingOrder;
        this.isSorted = true;
        this.isArchived = false;
        this.tags = tags;
    }

    /**
     * Sorts the notes in alphabetical order and returns them if there is a up (A-Z) / down (Z-A) command.
     * If tags exist, maps the tags to the HashMap and gets the corresponding notes
     * For each tag, there will be an ArrayList of the respective notes.
     * The method will then merge the notes in the ArrayLists into 1 large ArrayList.
     * ArrayList is then sorted and returned for the respective up/down commands
     *
     * @return noteString String containing the (filtered) notes (un)sorted
     */
    @Override
    public String execute() {

        ArrayList<Note> notes = new ArrayList<>();
        ArrayList<Note> pinned;
        ArrayList<Note> unpinned;


        if (isArchived) {
            notes = notebook.getArchivedNotes();
            return formatNotes(COMMAND_SUCCESSFUL_MESSAGE, notes);
        }

        if (!notebook.checkPinned() && tags == null) {
            if (!isSorted) {
                notes = notebook.getNotes();
            } else if (isSorted) {
                notes = notebook.getSortedList(isAscendingOrder, (Boolean) null);
            }

            if (notes.isEmpty()) {
                return Formatter.formatString(COMMAND_UNSUCCESSFUL_MESSAGE_EMPTY_NOTEBOOK);
            }

            return formatNotes(COMMAND_SUCCESSFUL_MESSAGE, notes);
        }

        if (notebook.checkPinned() && tags == null) {
            if (!isSorted) {
                pinned = notebook.getPinnedNotes();
                unpinned = notebook.getUnpinnedNotes();
            } else {
                pinned = notebook.getSortedList(isAscendingOrder, true);
                unpinned = notebook.getSortedList(isAscendingOrder, false);
            }

            return formatNotes(PINNED_MESSAGE, UNPINNED_MESSAGE, pinned, unpinned);
        }

        // Obtaining ArrayList<String> of tags and parsing it to get an ArrayList<Tag> of tags
        Map<Tag, ArrayList<Note>> tagMap = tagManager.getTagMap();
        ArrayList<Tag> tagList = new ArrayList<>();

        for (String tag : tags) {
            Tag currentTag = tagManager.getTag(tag);

            if (currentTag != null) {
                tagList.add(currentTag);
            }
        }

        // Check if the user inputted tags match any of the existing tags.
        if (tagList.isEmpty()) {
            return Formatter.formatString(COMMAND_UNSUCCESSFUL_MESSAGE_INVALID_TAG);
        }

        // Based on user inputted tags, will store the respective values (notes) in an ArrayList
        // E.g. if user input 2 tags, CS2113 and important, will have 2 ArrayList of notes
        //      1 for the values corresponding to CS2113 and the other for important tag
        ArrayList<ArrayList<Note>> values = new ArrayList<>();
        for (int i = 0; i < tagList.size(); i++) {
            values.add(tagMap.get(tagList.get(i)));
        }

        // Account for note duplicates (multiple tags).
        // For e.g. In case an item has both CS2113 and Important tag
        for (ArrayList<Note> value : values) {
            for (Note note : value) {
                if (!notes.contains(note)) {
                    notes.add(note);
                }
            }
        }

        // Checking for empty notes List
        if (notes.isEmpty()) {
            return Formatter.formatString(COMMAND_UNSUCCESSFUL_MESSAGE_EMPTY_NOTEBOOK);
        }

        if (!notebook.checkPinned() && tags != null) {
            if (isSorted) {
                // Sort the tagged notes
                ArrayList<Note> sortedTaggedNotes = new ArrayList<>();
                notes = notebook.getSortedList(isAscendingOrder, null, sortedTaggedNotes);
            }
            return formatNotes(COMMAND_SUCCESSFUL_MESSAGE, notes);
        }

        if (notebook.checkPinned() && tags != null) {
            if (!isSorted) {
                pinned = notebook.getPinnedNotes(notes);
                unpinned = notebook.getUnpinnedNotes(notes);
            } else {
                pinned = notebook.getSortedList(isAscendingOrder, true, notes);
                unpinned = notebook.getSortedList(isAscendingOrder, false, notes);
            }
            return formatNotes(PINNED_MESSAGE, UNPINNED_MESSAGE, pinned, unpinned);
        }

        return formatNotes(COMMAND_SUCCESSFUL_MESSAGE, notes);
    }
}