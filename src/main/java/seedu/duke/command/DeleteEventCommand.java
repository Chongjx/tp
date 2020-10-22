package seedu.duke.command;

import seedu.duke.data.timetable.Event;
import seedu.duke.ui.Formatter;

import java.util.ArrayList;

import static seedu.duke.util.PrefixSyntax.PREFIX_DELIMITER;
import static seedu.duke.util.PrefixSyntax.PREFIX_INDEX;

/**
 * This class allows us to store relevant information regarding the events to be deleted from the timetable and
 * manipulate it before executing it via the execute method.
 */
public class DeleteEventCommand extends Command {

    public static final String COMMAND_WORD = "delete-e";

    public static final String COMMAND_USAGE = COMMAND_WORD + ": Deletes an event. Parameters: "
            + PREFIX_DELIMITER + PREFIX_INDEX + " INDEX";

    public static final String COMMAND_SUCCESSFUL_MESSAGE = "Event deleted";
    public static final String COMMAND_UNSUCCESSFUL_MESSAGE = "Event failed to delete";
    public static final String INDEX_OUT_OF_RANGE_MESSAGE = "The index you specified is out of range. "
            + "Please specify the index that is indicated when you print the event list";

    private int index;

    /**
     * Constructs a DeleteEventCommand that specifies which index to delete.
     *
     * @param index Index written on list-e -1.
     */
    public DeleteEventCommand(int index) {
        this.index = index;
    }

    @Override
    public String execute() {
        ArrayList<String> returnMessages;
        if (index < 0 || index >= timetable.getEvents().size()) {
            returnMessages = new ArrayList<>();
            returnMessages.add(COMMAND_UNSUCCESSFUL_MESSAGE);
            returnMessages.add(INDEX_OUT_OF_RANGE_MESSAGE);
            return Formatter.formatString(returnMessages, true);
        }
        Event event = timetable.getEvent(index);
        timetable.deleteEvent(index);
        return Formatter.formatEventString(COMMAND_SUCCESSFUL_MESSAGE, event);
    }
}
