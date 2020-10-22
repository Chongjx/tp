package seedu.duke.command;

import seedu.duke.data.timetable.Event;
import seedu.duke.data.timetable.RecurringEvent;
import seedu.duke.util.DateTimeManager;
import seedu.duke.ui.Formatter;

import java.util.ArrayList;

import static seedu.duke.util.PrefixSyntax.PREFIX_DELIMITER;
import static seedu.duke.util.PrefixSyntax.PREFIX_RECURRING;
import static seedu.duke.util.PrefixSyntax.PREFIX_REMIND;
import static seedu.duke.util.PrefixSyntax.PREFIX_STOP_RECURRING;
import static seedu.duke.util.PrefixSyntax.PREFIX_TIMING;
import static seedu.duke.util.PrefixSyntax.PREFIX_TITLE;

/**
 * This class allows us to store relevant information regarding the events to add to the timetable and manipulate it
 * before executing it via the execute method.
 */
public class AddEventCommand extends Command {

    public static final String COMMAND_WORD = "add-e";

    public static final String COMMAND_USAGE = COMMAND_WORD + ": Adds an event to the timetable. Parameters:"
            + PREFIX_DELIMITER + PREFIX_TITLE + " TITLE "
            + PREFIX_DELIMITER + PREFIX_TIMING + " TIMING (Format: " + DateTimeManager.DATE_FORMAT + ") "
            + "[" + PREFIX_DELIMITER + PREFIX_RECURRING
            + String.format(" Frequency (%s, %s, %s, %s)] ",
            RecurringEvent.DAILY_RECURRENCE_TYPE, RecurringEvent.WEEKLY_RECURRENCE_TYPE,
            RecurringEvent.MONTHLY_RECURRENCE_TYPE, RecurringEvent.YEARLY_RECURRENCE_TYPE)
            + "[" + PREFIX_DELIMITER + PREFIX_REMIND + " [Days before (Default: 1)]" + "] "
            + "[" + PREFIX_DELIMITER + PREFIX_STOP_RECURRING + " TIMING (Format: " + DateTimeManager.DATE_FORMAT + ")]";

    public static final String COMMAND_SUCCESSFUL_MESSAGE = "Added the following!";
    // No COMMAND_UNSUCCESSFUL_MESSAGE as we do not expect failure to occur at this stage.

    private Event event;

    /**
     * Constructor that takes in the event to be written to the timetable.
     *
     * @param event Event to be written to the timetable.
     */
    public AddEventCommand(Event event) {
        this.event = event;
    }

    @Override
    public String execute() {
        timetable.addEvent(event);
        return Formatter.formatEventString(COMMAND_SUCCESSFUL_MESSAGE, event);
    }
}
