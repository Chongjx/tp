package seedu.notus.data.timetable;

import seedu.notus.data.tag.TaggableObject;
import seedu.notus.ui.Formatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

//@@author brandonywl
/**
 * Represents an Event. Contains all the information of an Event.
 */
public class Event extends TaggableObject implements Comparable<Event> {
    public static final String REMINDER_DAY = "day";
    public static final String REMINDER_WEEK = "week";

    private String title;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Boolean isToRemind;
    private Boolean isRecurring;
    private HashMap<String, ArrayList<Integer>> reminderPeriods = new HashMap<>();

    /**
     * Creates an Event object with its title, date and time provided.
     *
     * @param title Title of the event.
     * @param startDateTime Start DateTime of the event.
     */
    public Event(String title, LocalDateTime startDateTime) {
        this.title = title;
        this.startDateTime = startDateTime;
        this.isToRemind = false;
        this.isRecurring = false;
    }

    /**
     * Creates an Event object with its title, date, time, isToRemind and isRecurring provided.
     *
     * @param title Title of the event.
     * @param startDateTime Start DateTime of the event.
     * @param isToRemind Whether the event requires a reminder.
     * @param isRecurring Whether the event will re-occur.
     */
    public Event(String title, LocalDateTime startDateTime, boolean isToRemind, boolean isRecurring) {
        this(title, startDateTime);
        this.isToRemind = isToRemind;
        this.isRecurring = isRecurring;
    }

    /**
     * Creates an Event object with its title, date, time, isToRemind, isRecurring as well as time before reminder and
     * time unit provided.
     *
     * @param title Title of the event.
     * @param startDateTime Start DateTime of the event.
     * @param isToRemind Whether event is set to remind.
     * @param isRecurring Whether event is set to re-occur.
     * @param timePeriods Time periods to remind about this event before it occurs.
     * @param timeUnits Time units to remind about this event before it occurs.
     */
    public Event(String title, LocalDateTime startDateTime, boolean isToRemind, boolean isRecurring,
                 ArrayList<Integer> timePeriods, ArrayList<String> timeUnits) {
        this(title, startDateTime, isToRemind, isRecurring);

        assert timePeriods.size() == timeUnits.size() : "Something is wrong with the parser! Check it out.";

        for (int i = 0; i < timePeriods.size(); i++) {
            String timeUnit = timeUnits.get(i);
            ArrayList<Integer> storedReminders = reminderPeriods.get(timeUnit);
            if (storedReminders == null) {
                storedReminders = new ArrayList<>();
            }
            storedReminders.add(timePeriods.get(i));
            Collections.sort(storedReminders);
            reminderPeriods.put(timeUnit, storedReminders);
        }

    }

    public Event(String title, LocalDateTime startDateTime, boolean isToRemind, boolean isRecurring,
                  HashMap<String, ArrayList<Integer>> reminderPeriods) {
        this(title, startDateTime, isToRemind, isRecurring);
        this.reminderPeriods = reminderPeriods;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime dateTime) {
        startDateTime = dateTime;
    }

    public LocalDate getStartDate() {
        return startDateTime.toLocalDate();
    }

    public LocalTime getStartTime() {
        return startDateTime.toLocalTime();
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime dateTime) {
        endDateTime = dateTime;
    }

    public LocalDate getEndDate() {
        return endDateTime.toLocalDate();
    }

    public LocalTime getEndTime() {
        return endDateTime.toLocalTime();
    }

    public boolean getIsToRemind() {
        return isToRemind;
    }

    public void setIsToRemind(boolean isToRemind) {
        this.isToRemind = isToRemind;
    }

    public HashMap<String, ArrayList<Integer>> getReminderPeriods() {
        return this.reminderPeriods;
    }

    public void setReminderPeriods(HashMap<String, ArrayList<Integer>> reminderPeriods) {
        this.reminderPeriods = reminderPeriods;
    }

    public boolean getRecurring() {
        return isRecurring;
    }

    /**
     * Get all the reminder dates from that this Event would have.
     *
     * @return An ArrayList of Dates that reminders of this event should show.
     */

    public ArrayList<LocalDate> getReminderDates() {
        ArrayList<LocalDate> dates = new ArrayList<>();
        if (!isToRemind) {
            return dates;
        }
        for (String unit : reminderPeriods.keySet()) {
            ArrayList<Integer> timePeriodsInUnit = reminderPeriods.get(unit);
            LocalDate date = this.startDateTime.toLocalDate();
            for (Integer timePeriod : timePeriodsInUnit) {
                switch (unit) {
                case REMINDER_DAY:
                    date = this.startDateTime.toLocalDate().plusDays(-timePeriod);
                    break;
                case REMINDER_WEEK:
                    date = this.startDateTime.toLocalDate().plusWeeks(-timePeriod);
                    break;
                default:
                    break;
                }
                dates.add(date);
            }
        }
        dates.sort(LocalDate::compareTo);
        return dates;
    }

    /**
     * Converts an Event to a format for a reminder.
     *
     * @return Reduced String representation of an Event.
     */
    public String toReminderString() {
        String titleString = "Event: " + title;
        String dateString = "Date: " + startDateTime.toLocalDate().toString()
                + "\tTime: " + startDateTime.toLocalTime().toString();
        return titleString + Formatter.LS + dateString;
    }

    @Override
    public String toString() {
        String titleString = "Event: " + title;
        String dateString = "Date: " + startDateTime.toLocalDate().toString()
                + "\tTime: " + startDateTime.toLocalTime().toString();
        String remindString = "Reminder: " + isToRemind;
        String repeatingString = "Repeating: " + isRecurring;
        String lineSeparator = Formatter.LS;
        return titleString + lineSeparator + dateString + lineSeparator + remindString
                + lineSeparator + repeatingString;
    }

    /**
     * Provides an ArrayList representation of the event for the formatter to use.
     *
     * @return ArrayList of strings to represent the event.
     */
    public ArrayList<String> toStringArray() {
        ArrayList<String> result = new ArrayList<>();
        result.add("Event: " + title);
        result.add("Date: " + startDateTime.toLocalDate().toString()
                + "\tTime: " + startDateTime.toLocalTime().toString());
        result.add("Reminder: " + isToRemind);
        result.add("Repeating: " + isRecurring);
        return result;
    }

    /**
     * Converts an array list of events to an indexed list of events. Each line of event is converted to use
     * arraylist for the formatter.
     *
     * @param events Events to be listed.
     * @return ArrayList String representation of the list of events.
     */
    public static ArrayList<String> calendarToString(ArrayList<Event> events) {
        ArrayList<String> results = new ArrayList<>();
        events.sort(Comparator.comparing(Event::getStartDate).thenComparing(Event::getStartTime));
        int i = 1;
        for (Event event : events) {
            ArrayList<String> eventString = event.toStringArray();
            eventString.set(0, i++ + ". " + eventString.get(0));
            results.addAll(eventString);
            results.add(" ");
        }
        results.remove(results.size() - 1);
        return results;
    }

    /**
     * Converts all events in the ArrayList provided into stringArray format, appends them together and returns it.
     *
     * @param events Events in the same time period.
     * @return Non-index ArrayList of strings to represent all events in this time period.
     */
    public static ArrayList<String> calendarToStringNoIndex(ArrayList<Event> events) {
        ArrayList<String> results = new ArrayList<>();
        events.sort(Comparator.comparing(Event::getStartDate).thenComparing(Event::getStartTime));
        for (Event event : events) {
            ArrayList<String> eventString = event.toStringArray();
            results.addAll(eventString);
            results.add(" ");
        }
        return results;
    }

    /**
     * Converts a HashMap calendar to an ArrayList of strings representing all events in the calendar.
     * Provides a day wrapper around the calendarToStringNoIndex.
     *
     * @param calendar Calendar storing events to print.
     * @return ArrayList of Strings to be sequentially formatted in the formatter.
     */
    public static ArrayList<String> monthCalendarToString(HashMap<Integer, ArrayList<Event>> calendar) {
        ArrayList<String> results = new ArrayList<>();
        ArrayList<Integer> days = new ArrayList<>(calendar.keySet());
        Collections.sort(days);
        for (Integer day : days) {
            results.addAll(calendarToStringNoIndex(calendar.get(day)));
        }
        return results;
    }

    /**
     * Converts a HashMap calendar to an ArrayList of strings representing all events in the calendar.
     * Provides a month comparison around monthCalendarToString.
     *
     * @param calendar Calendar storing events to print.
     * @return ArrayList of Strings to be sequentially formatted in the formatter.
     */
    public static ArrayList<String> yearCalendarToString(HashMap<Month, HashMap<Integer, ArrayList<Event>>> calendar) {
        ArrayList<String> results = new ArrayList<>();
        ArrayList<Month> months = new ArrayList<>(calendar.keySet());
        months.sort(Month::compareTo);
        for (Month month: months) {
            results.add(month.name());
            results.addAll(monthCalendarToString(calendar.get(month)));
            results.add(" ");
        }
        results.remove(results.size() - 1);
        results.remove(results.size() - 1);
        return results;
    }

    @Override
    public int compareTo(Event o) {
        return startDateTime.compareTo(o.startDateTime);
    }
}
