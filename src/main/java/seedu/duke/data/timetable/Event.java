package seedu.duke.data.timetable;

import seedu.duke.data.exception.SystemException;
import seedu.duke.ui.InterfaceManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents an Event. Contains all the information of an Event.
 */
public class Event {
    private static final String REMINDER_DAY = "day";
    private static final String REMINDER_WEEK = "week";
    private static final String REMINDER_MONTH = "month";

    private String title;
    // Can be combined into LocalDateTime
    private LocalDate date;
    private LocalTime time;
    private Boolean isToRemind;
    private Boolean isRecurring;
    private int timeBeforeReminder = 1;
    private String timeUnit = REMINDER_DAY;

    /**
     * Creates an Event object with its title, date and time provided.
     *
     * @param title Title of the event.
     * @param date Date of the event.
     * @param time Time of the event.
     */
    public Event(String title, LocalDate date, LocalTime time) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.isToRemind = false;
        this.isRecurring = false;
    }

    /**
     * Creates an Event object with its title, date, time, isToRemind and isRecurring provided.
     *
     * @param title Title of the event.
     * @param date Date of the event.
     * @param time Time of the event.
     * @param isToRemind Whether the event requires a reminder.
     * @param isRecurring Whether the event will re-occur.
     */
    public Event(String title, LocalDate date, LocalTime time, boolean isToRemind, boolean isRecurring) {
        this(title, date, time);
        this.isToRemind = isToRemind;
        this.isRecurring = isRecurring;
    }

    /**
     * Creates an Event object with its title, date, time, isToRemind, isRecurring as well as time before reminder and
     * time unit provided.
     *
     * @param title Title of the event.
     * @param date Date of the event.
     * @param time Time of the event.
     * @param isToRemind Whether event is set to remind.
     * @param isRecurring Whether event is set to re-occur.
     * @param timeBeforeReminder How much time units before event date to remind the user.
     * @param timeUnit What time unit to use.
     */
    public Event(String title, LocalDate date, LocalTime time, boolean isToRemind, boolean isRecurring,
                 int timeBeforeReminder, String timeUnit) {
        this(title, date, time);
        this.isToRemind = isToRemind;
        this.isRecurring = isRecurring;
        this.timeBeforeReminder = timeBeforeReminder;
        this.timeUnit = timeUnit;
    }

    /**
     * Creates an Event object with its title, dateTime, isToRemind and isRecurring provided. Used for event
     * constructing with user input. Has default reminder time of 1 day before event.
     *
     * @param title Title of event.
     * @param dateTime LocalDateTime provided by user input.
     * @param isToRemind Whether event is set to remind.
     * @param isRecurring Whether event is set to re-occur.
     */
    public Event(String title, LocalDateTime dateTime, boolean isToRemind, boolean isRecurring) {
        this(title, dateTime.toLocalDate(), dateTime.toLocalTime(), isToRemind, isRecurring);
    }

    /**
     * Creates an Event object with its title, dateTime, isToRemind, isRecurring as well as time before reminder and
     * the time unit provided. Used for event construction with user input.
     * @param title Title of event.
     * @param dateTime LocalDateTime provided by user input.
     * @param isToRemind Whether event is set to remind.
     * @param isRecurring Whether event is set to re-occur.
     * @param timeBeforeReminder How many time units before event date to remind the user.
     * @param timeUnit Expected to be day, week, month.
     */
    public Event(String title, LocalDateTime dateTime, boolean isToRemind, boolean isRecurring, int timeBeforeReminder,
                 String timeUnit) {
        this(title, dateTime.toLocalDate(), dateTime.toLocalTime(), isToRemind, isRecurring, timeBeforeReminder,
                timeUnit);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean getToRemind() {
        return isToRemind;
    }

    public void setToRemind(boolean toRemind) {
        isToRemind = toRemind;
    }

    public boolean getRecurring() {
        return isRecurring;
    }

    public int getTimeBeforeReminder() {
        return timeBeforeReminder;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public boolean checkTimeToRemind(LocalDate currentDate) throws SystemException {
        switch (timeUnit) {
        case REMINDER_DAY:
            if (currentDate.plusDays(timeBeforeReminder).equals(date)) {
                return true;
            }
            break;
        case REMINDER_WEEK:
            if (currentDate.plusWeeks(timeBeforeReminder).equals(date)) {
                return true;
            }
            break;
        case REMINDER_MONTH:
            if (currentDate.plusMonths(timeBeforeReminder).equals(date)) {
                return true;
            }
            break;
        default:
            throw new SystemException(SystemException.ExceptionType.EXCEPTION_WRONG_TIME_UNIT);
        }
        return false;
    }

    public String toReminderString() {
        String titleString = "Event: " + title;
        String dateString = "Date: " + date.toString() + "\tTime: " + time.toString();
        return titleString + InterfaceManager.LS + dateString;
    }

    @Override
    public String toString() {
        String titleString = "Event: " + title;
        String dateString = "Date: " + date.toString() + "\tTime: " + time.toString();
        String remindString = "Reminder: " + isToRemind;
        String repeatingString = "Repeating: " + isRecurring;
        String lineSeparator = InterfaceManager.LS;
        return titleString + lineSeparator + dateString + lineSeparator + remindString
                + lineSeparator + repeatingString;
    }
}
