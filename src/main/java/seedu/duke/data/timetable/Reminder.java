package seedu.duke.data.timetable;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A reminder class that holds the event to remind as well as the date to remind.
 */
public class Reminder implements Comparable<Reminder> {
    private Event event;
    private LocalDate dateToRemind;

    /**
     * Constructor for a Reminder. Includes what event to remind about as well as what date to remind on.
     *
     * @param event Event to remind
     * @param dateToRemind Date to remind.
     */
    public Reminder(Event event, LocalDate dateToRemind) {
        this.event = event;
        this.dateToRemind = dateToRemind;
    }

    public Event getEvent() {
        return event;
    }

    /**
     * Returns whether this reminder should be raised to the user.
     * If the date provided is equal the dateToRemind stored in the reminder, it returns true.
     *
     * @param date Date to check
     * @return Whether the reminder should be raised.
     */
    public boolean toRemind(LocalDate date) {
        return dateToRemind.equals(date);
    }

    /**
     * Provides a way to check if the reminder should have already been reminded after that date.
     *
     * @param date Date to check.
     * @return Whether the reminder should have already been raised.
     */
    public boolean reminderDue(LocalDate date) {
        return dateToRemind.compareTo(date) <= 0;
    }

    @Override
    public String toString() {
        return event.toReminderString();
    }

    @Override
    public int compareTo(Reminder reminder) {
        return dateToRemind.compareTo(reminder.dateToRemind);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof Reminder) {
            Reminder reminder = ((Reminder) object);
            return reminder.dateToRemind.equals(dateToRemind) && reminder.event.getTitle().equals(event.getTitle());
        } else {
            return false;
        }
    }
}
