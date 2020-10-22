package seedu.duke.data.timetable;

import seedu.duke.ui.Formatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class RecurringEvent extends Event {
    private LocalDate endRecurrenceDate;
    private String recurrenceType;

    public static final LocalDate DEFAULT_END_RECURRENCE = LocalDate.of(3000, 12, 31);
    public static final String DAILY_RECURRENCE_TYPE = "daily";
    public static final String WEEKLY_RECURRENCE_TYPE = "weekly";
    public static final String MONTHLY_RECURRENCE_TYPE = "monthly";
    public static final String YEARLY_RECURRENCE_TYPE = "yearly";

    public RecurringEvent(String title, LocalDateTime dateTime, boolean isToRemind, LocalDate endRecurrenceDate,
                          String recurrenceType, ArrayList<Integer> timePeriods, ArrayList<String> timeUnits) {
        super(title, dateTime, isToRemind, true, timePeriods, timeUnits);
        if (endRecurrenceDate == null) {
            endRecurrenceDate = DEFAULT_END_RECURRENCE;
        }
        this.endRecurrenceDate = endRecurrenceDate;
        this.recurrenceType = recurrenceType;
    }

    public RecurringEvent(String title, LocalDateTime dateTime, boolean isToRemind, String recurrenceType,
                          ArrayList<Integer> timePeriods, ArrayList<String> timeUnits) {
        this(title, dateTime, isToRemind, DEFAULT_END_RECURRENCE, recurrenceType, timePeriods, timeUnits);
    }


    public RecurringEvent(String title, LocalDateTime dateTime, boolean isToRemind, LocalDate endRecurrenceDate,
                          String recurrenceType, HashMap<String, ArrayList<Integer>> reminderPeriods) {
        super(title, dateTime, isToRemind, true, reminderPeriods);
        if (endRecurrenceDate == null) {
            endRecurrenceDate = DEFAULT_END_RECURRENCE;
        }
        this.endRecurrenceDate = endRecurrenceDate;
        this.recurrenceType = recurrenceType;
    }

    public RecurringEvent(String title, LocalDateTime dateTime, boolean isToRemind, String recurrenceType,
                          HashMap<String, ArrayList<Integer>> reminderPeriods) {
        this(title, dateTime, isToRemind, DEFAULT_END_RECURRENCE, recurrenceType, reminderPeriods);
    }

    /**
     * Checks if the event should still re-occur at a specified date.
     *
     * @param date Date to check.
     * @return Whether to re-occur or not.
     */
    public boolean checkAfterEndRecurrence(LocalDate date) {
        return (endRecurrenceDate.compareTo(date) < 0);
    }

    /**
     * Checks whether the current RecurringEvent object will re-occur between the start and end date (inclusive)
     * and return an ArrayList that contains an event for all the times that it re-occurs.
     *
     * @param startDate Start date of the time period to check (inclusive)
     * @param endDate End date of the time period to check (inclusive)
     * @return ArrayList of this event at different dates at a specified interval within the time period.
     */
    public ArrayList<Event> getRecurrences(LocalDate startDate, LocalDate endDate) {
        ArrayList<Event> eventSet = new ArrayList<>();
        while (startDate.compareTo(endDate) <= 0) {
            if (checkAfterEndRecurrence(startDate)) {
                return eventSet;
            }
            if (toReoccur(startDate)) {
                LocalDateTime dateTime = LocalDateTime.of(startDate, getTime());
                Event event = new Event(getTitle(), dateTime, getToRemind(), false, getReminderPeriod());
                eventSet.add(event);
            }
            startDate = startDate.plusDays(1);
        }
        return eventSet;
    }

    /**
     * Check if the event is to reoccur on a specific date.
     *
     * @param date Date to check.
     * @return Whether it will reoccur
     */
    public boolean toReoccur(LocalDate date) {
        LocalDate eventDate = getDate();
        while (eventDate.compareTo(date) < 0) {
            eventDate = timeStep(eventDate);
        }
        return eventDate.equals(date);
    }

    @Override
    public String toString() {
        String endRecurrenceString = "Until: ";
        if (!endRecurrenceDate.equals(DEFAULT_END_RECURRENCE)) {
            endRecurrenceString = endRecurrenceString.concat(endRecurrenceDate.toString());
        } else {
            endRecurrenceString = endRecurrenceString.concat("Forever");
        }
        return super.toString() + String.format(" (%s)", recurrenceType) + Formatter.LS + endRecurrenceString;
    }

    public String getEndRecurrenceString() {
        String endRecurrenceString = "Until: ";
        String timing = (!endRecurrenceDate.equals(DEFAULT_END_RECURRENCE)) ? endRecurrenceDate.toString() : "Forever";
        endRecurrenceString = endRecurrenceString.concat(timing);
        return String.format(" (%s)", endRecurrenceString);
    }

    /**
     * Provides a time step of a specified date by one time unit and return it as a LocalDate object.
     *
     * @param date Date to step forward
     * @return Future date
     */
    public abstract LocalDate timeStep(LocalDate date);

}
