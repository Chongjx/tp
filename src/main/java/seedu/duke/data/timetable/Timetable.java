package seedu.duke.data.timetable;

import seedu.duke.util.DateTimeManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Represents a TimeTable object. Contains all the events.
 */
public class Timetable {

    private ArrayList<Event> events;
    private ArrayList<Event> nonRecurringEvents;
    private ArrayList<DailyEvent> dailyEvents;
    private ArrayList<WeeklyEvent> weeklyEvents;
    private ArrayList<MonthlyEvent> monthlyEvents;
    private ArrayList<YearlyEvent> yearlyEvents;

    /**
     * Creates a new list of events.
     */
    public Timetable() {
        events = new ArrayList<>();
        nonRecurringEvents = new ArrayList<>();
        dailyEvents = new ArrayList<>();
        weeklyEvents = new ArrayList<>();
        monthlyEvents = new ArrayList<>();
        yearlyEvents = new ArrayList<>();
    }

    /**
     * Creates a list of events from existing data.
     *
     * @param events from existing data.
     */
    public Timetable(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public Event getEvent(int index) {
        return events.get(index);
    }

    /**
     * Getter for JUnit test.
     * @return Stored daily events.
     */
    public ArrayList<DailyEvent> getDailyEvents() {
        return dailyEvents;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    /**
     * Adds an event into the timetable.
     *
     * @param event to be added.
     */
    public void addEvent(Event event) {
        events.add(event);
        if (!event.getRecurring()) {
            nonRecurringEvents.add(event);
        } else {
            if (event instanceof DailyEvent) {
                dailyEvents.add((DailyEvent) event);
            } else if (event instanceof WeeklyEvent) {
                weeklyEvents.add((WeeklyEvent) event);
            } else if (event instanceof MonthlyEvent) {
                monthlyEvents.add((MonthlyEvent) event);
            } else if (event instanceof YearlyEvent) {
                yearlyEvents.add((YearlyEvent) event);
            }
        }
    }

    /**
     * Removes an event from the timetable.
     *
     * @param index Index to be removed.
     */
    public void deleteEvent(int index) {
        Event event = events.get(index);
        events.remove(index);
        if (event instanceof DailyEvent) {
            dailyEvents.remove(event);
        } else if (event instanceof WeeklyEvent) {
            weeklyEvents.remove(event);
        } else if (event instanceof MonthlyEvent) {
            monthlyEvents.remove(event);
        } else if (event instanceof YearlyEvent) {
            yearlyEvents.remove(event);
        } else {
            nonRecurringEvents.remove(event);
        }
    }

    /**
     * Gets the timetable for a specified year. Includes multiple recurrent events.
     *
     * @param year Year to check for timetable
     * @return A HashMap that maps the name of the month to a HashMap that maps date to list of events that occurs on
     *      that day, sorted by starting time.
     */
    public HashMap<String, HashMap<Integer, ArrayList<Event>>> getYearTimetable(int year) {
        HashMap<String, HashMap<Integer, ArrayList<Event>>> calendar = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            LocalDate startDate = LocalDate.of(year, i, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            calendar.putAll(getTimetable(startDate, endDate));
        }
        return calendar;
    }

    /**
     * Gets the timetable for a specified month and year. Includes multiple recurrent events.
     *
     * @param year Year to check for timetable
     * @param month Month to check for timetable
     * @return A HashMap that maps the name of the month to a HashMap that maps date to list of events that occurs on
     *      that day, sorted by starting time.
     */
    public HashMap<String, HashMap<Integer, ArrayList<Event>>> getMonthTimetable(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return getTimetable(startDate, endDate);
    }

    /**
     * Gets the timetable for a specified time period. Includes multiple recurrent events. Sorting does not work yet.
     *
     * @param startDate Date to start checking for events.
     * @param endDate Date to stop checking for events.
     * @return A HashMap that maps the name of the month to a HashMap that maps date to list of events that occurs on
     *      that day, sorted by starting time.
     */
    public HashMap<String, HashMap<Integer, ArrayList<Event>>> getTimetable(LocalDate startDate, LocalDate endDate) {
        // Get an eventSet of all events occurring during the specified time period.
        ArrayList<Event> eventSet = getAllEvents(startDate, endDate);

        HashMap<String, HashMap<Integer,ArrayList<Event>>> calendar = new HashMap<>();

        // Map all events to their relevant date and month. Sort by startTime.
        for (Event event : eventSet) {
            String month = DateTimeManager.getMonthName(event.getDate());
            int date = event.getDate().getDayOfMonth();
            // Get a HashMap for the specified month. If it has not been initialized, initialize one.
            HashMap<Integer, ArrayList<Event>> monthEvents = calendar.get(month);
            if (monthEvents == null) {
                monthEvents = new HashMap<>();
            }
            // Get the ArrayList representing Events occurring on that date of the month.
            // If it has not been initialized, initialize one. Sorts the list after insertion.
            ArrayList<Event> dailyEvents = monthEvents.get(date);
            if (dailyEvents == null) {
                dailyEvents = new ArrayList<>();
            }
            dailyEvents.add(event);
            // Sorting doesn't work.
            Collections.sort(dailyEvents);

            monthEvents.put(date, dailyEvents);
            calendar.put(month, monthEvents);
        }
        return calendar;
    }

    /**
     * Provides a method to get all events, including re-occurring events in an arraylist in a specified period.
     *
     * @param startDate Start of time period.
     * @param endDate End of time period.
     * @return ArrayList of all events. Re-occurring events are initialized as a new event.
     */
    public ArrayList<Event> getAllEvents(LocalDate startDate, LocalDate endDate) {
        ArrayList<Event> eventSet = new ArrayList<>();
        eventSet.addAll(getNonRecurringEvents(startDate, endDate, nonRecurringEvents));
        eventSet.addAll(getAllRecurringEvents(startDate, endDate,
                dailyEvents, weeklyEvents, monthlyEvents, yearlyEvents));
        return eventSet;
    }

    /**
     * Given a set of events, check if they will occur during a time period specified by the start and end period.
     *
     * @param startDate Start date of period to check whether the event will occur. Inclusive of the date.
     * @param endDate End date to check whether the event will occur. Inclusive of the date.
     * @param nonRecurringSet Set of non-recurring events.
     * @return ArrayList of Events that occur between the specified time period.
     */
    private ArrayList<Event> getNonRecurringEvents(LocalDate startDate, LocalDate endDate,
                                                   ArrayList<Event> nonRecurringSet) {
        ArrayList<Event> eventSet = new ArrayList<>();
        for (Event event : nonRecurringSet) {
            LocalDate eventDate = event.getDate();
            if (eventDate.compareTo(startDate) >= 0 && eventDate.compareTo(endDate) <= 0) {
                eventSet.add(event);
            }
        }
        return eventSet;
    }

    /**
     * Given an ArrayList of events, get all events that will occur during the specified time period.
     *
     * @param startDate Start date of the period to check for recurrence. Inclusive of the date.
     * @param endDate End date of the period to check for recurrence. Inclusive of the date.
     * @param events List of events that are recurrent.
     * @return An ArrayList of Events of all events that will occur between the two specified time periods.
     */
    private ArrayList<Event> getRecurringEvents(LocalDate startDate, LocalDate endDate,
                                                  ArrayList<? extends RecurringEvent> events) {
        ArrayList<Event> eventList = new ArrayList<>();
        for (RecurringEvent event : events) {
            eventList.addAll(event.getRecurrences(startDate, endDate));
        }
        return eventList;
    }

    /**
     * Gets all instances of events that will reoccur between the specified time period. Provides a Varargs signature
     * of getRecurringEvents, thus simplifying getting all events in the timetable in a time period.
     *
     * @param startDate Start of the time period. Inclusive.
     * @param endDate End of the time period. Inclusive.
     * @param eventsSet ArrayList of Events that should extend from RecurringEvent.
     * @return ArrayList of Events that will occur during the time period. If recurring events are set, they will be
     *      in the arraylist as a Event, not as an extension of RecurringEvent.
     */
    @SafeVarargs
    protected final ArrayList<Event> getAllRecurringEvents(LocalDate startDate, LocalDate endDate,
                                                  ArrayList<? extends RecurringEvent>... eventsSet) {
        ArrayList<Event> eventList = new ArrayList<>();
        for (ArrayList<? extends RecurringEvent> events : eventsSet) {
            eventList.addAll(getRecurringEvents(startDate, endDate, events));
        }
        return eventList;
    }

    /**
     * Returns a PriorityQueue of Reminders from a set of events. PriorityQueue is sorted by their dates.
     *
     * @param setOfEvents Set of Events to search from.
     * @return PriorityQueue of Reminder from all provided events.
     */
    public PriorityQueue<Reminder> getEventSetReminder(ArrayList<Event> setOfEvents) {
        PriorityQueue<Reminder> reminders = new PriorityQueue<>(Reminder::compareTo);
        for (Event event : setOfEvents) {
            for (LocalDate reminderDate : event.getReminderDates()) {
                reminders.add(new Reminder(event, reminderDate));
            }
        }
        return reminders;
    }


    /**
     * A method that gets all reminders that should go off today. It takes in events for the next 1 month and gets all
     * their reminders.
     *
     * @return An ArrayList that contains all the reminders to go off today.
     */
    public ArrayList<Reminder> getReminders() {
        LocalDate today = LocalDate.now();
        // As reminders are set to maximum 1 week earlier,
        // we play it safe by looking for all 1 events in a one month time period.
        LocalDate endDate = today.plusMonths(1);
        ArrayList<Event> eventSet = getAllEvents(today, endDate);
        ArrayList<Reminder> todayReminders = new ArrayList<>();
        PriorityQueue<Reminder> reminders = getEventSetReminder(eventSet);
        while (reminders.size() > 0 && reminders.peek().reminderDue(today)) {
            Reminder reminder = reminders.poll();
            assert reminder != null;
            if (reminder.toRemind(today)) {
                todayReminders.add(reminder);
            }
        }
        return todayReminders;
    }

}
