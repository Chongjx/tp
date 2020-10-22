package seedu.duke.ui;

import com.diogonunes.jcolor.Attribute;
import seedu.duke.command.AddEventCommand;
import seedu.duke.data.notebook.Note;
import seedu.duke.data.timetable.RecurringEvent;
import seedu.duke.data.timetable.Reminder;
import seedu.duke.data.timetable.Timetable;
import seedu.duke.data.timetable.Event;
import seedu.duke.data.timetable.Timetable;

import java.util.ArrayList;
import java.util.Stack;

import static com.diogonunes.jcolor.Ansi.POSTFIX;
import static com.diogonunes.jcolor.Ansi.PREFIX;
import static com.diogonunes.jcolor.Ansi.RESET;
import static com.diogonunes.jcolor.Ansi.colorize;

public class Formatter {

    /**
     * A platform independent line separator.
     */
    public static final String LS = System.lineSeparator();

    private static final String ROW_SPLIT = "=";
    private static final String COLUMN_SPLIT = "|";
    private static final String COLUMN_START = "|| ";
    private static final String COLUMN_END = " ||";
    private static final String EMPTY_STRING = " ";
    private static final char EMPTY_CHAR = ' ';

    /**
     * Maximum number of characters within a row.
     */
    private static final int MAX_ROW_LENGTH = 100;
    /**
     * Maximum length of message to within a row, minus the start and end formatting.
     */
    private static final int MAX_MESSAGE_LENGTH = MAX_ROW_LENGTH - COLUMN_START.length() - COLUMN_END.length();
    /**
     * Length of a ansi defined color.
     */
    private static final int ANSI_PREFIX_LENGTH = 5;

    /**
     * Method compiles the ArrayList items and appends the items to a String.
     *
     * @return noteString StringBuilder containing the notes ready to be printed
     */
    public static String formatNotes(String pinnedHeader, String unpinnedHeader,
                                     ArrayList<Note> pinned, ArrayList<Note> unpinned) {
        String formattedString = "";
        formattedString = formatNotes(pinnedHeader, pinned);
        formattedString = formattedString.concat(formatNotes(unpinnedHeader, unpinned));
        return formattedString;
    }

    /**
     * Method compiles the ArrayList items and appends the items to a String.
     *
     * @param notes ArrayList of notes to obtain note title/tags from
     * @return noteString StringBuilder containing the notes ready to be printed
     */
    public static String formatNotes(String header, ArrayList<Note> notes) {
        String formattedString = "";
        int i = 1;

        formattedString = formattedString.concat(generatesHeader(header));

        for (Note note: notes) {
            String colorText = colorize(i + ". Title: " + note.getTitle() + " "
                    + note.getTagsName(), Attribute.BRIGHT_CYAN_TEXT());
            formattedString = formattedString.concat(colorText);

            int truncatedContentLength = Math.min(note.getContent().get(0).length(), MAX_MESSAGE_LENGTH - 50);

            String truncatedContent = note.getContent().get(0).substring(0, truncatedContentLength).concat("...");
            formattedString = formattedString.concat(LS + truncatedContent + LS);
            formattedString = formattedString.concat(generatesRowSplit());

            i++;
        }
        return encloseTopAndBottom(formattedString);
    }

    public static String formatNote(String header, Note note) {
        String formattedString = "";

        header = header.concat(" " + note.getTagsName());

        formattedString = formattedString.concat(generatesHeader(header));
        for (String line : note.getContent()) {
            formattedString = formattedString.concat(encloseRow(line));
        }
        return encloseTopAndBottom(formattedString);
    }

    public static String formatTimetable(String header, Timetable timetable) {
        String formattedString = "";
        return formattedString;
    }

    public static String formatEvent(String header, Event event) {
        String formattedString = "";
        return formattedString;
    }

    /**
     * Formats a provided event to an ArrayList format.
     *
     * @param event Event to be formatted
     * @return ArrayList of Strings to represent the Event.
     */
    public static ArrayList<String> formatEvent(Event event) {
        ArrayList<String> result = new ArrayList<>();
        result.add("Event: " + event.getTitle());
        result.add("Date: " + event.getDate().toString() + "\tTime: " + event.getTime().toString());
        result.add("Reminder: " + event.getToRemind());
        String repeatingString = "Repeating: " + event.getRecurring();
        String endRecurrenceDateString = "";
        if (event instanceof RecurringEvent) {
            RecurringEvent recurringEvent = (RecurringEvent) event;
            endRecurrenceDateString = recurringEvent.getEndRecurrenceString();
        }
        result.add(repeatingString + endRecurrenceDateString);
        return result;
    }

    public static String formatEventString(Event event) {
        return formatString(formatEvent(event), false);
    }

    /**
     * Provides a wrapper around formatEvent to add a header at the head of the ArrayList.
     *
     * @param event Event to be formatted
     * @param header Header to be placed at the front.
     * @return ArrayList of Strings to represent the Event.
     */
    public static String formatEventString(String header, Event event) {
        ArrayList<String> result = formatEvent(event);
        result.add(0, header);
        return formatString(result, true);
    }


    /**
     * Converts a header and an ArrayList of reminders into a formatted string to be printed.
     *
     *
     * @param header Success message to print.
     * @param reminders Reminders to be printed
     * @return String representation of all reminders to be shown.
     */
    public static String formatReminders(String header, ArrayList<Reminder> reminders) {
        ArrayList<String> result = new ArrayList<>();
        result.add(header);
        for (Reminder reminder : reminders) {
            result.addAll(formatReminder(reminder));
            result.add(" ");
        }
        result.remove(result.size() - 1);

        return formatString(result, true);
    }

    /**
     * Formats a provided event to an ArrayList format.
     *
     * @param reminder Reminder to be formatted.
     * @return ArrayList of Strings to represent the Reminder.
     */
    public static ArrayList<String> formatReminder(Reminder reminder) {
        Event event = reminder.getEvent();
        ArrayList<String> result = new ArrayList<>();
        result.add("Event: " + event.getTitle());
        result.add("Date: " + event.getDate().toString() + "\tTime: " + event.getTime().toString());
        return result;
    }

    /**
     * Formats a string to be printed out.
     *
     * @param message String to be formatted.
     * @return Formatted message.
     */
    public static String formatString(String message) {
        return encloseTopAndBottom(encloseRow(message));
    }

    /**
     * Formats an arraylist of strings to be printed out. Each element in the list will be printed in a newline.
     *
     * @param messages  Arraylist of strings to be formatted.
     * @param hasHeader Determines if there is a header. Header MUST be the first element in the list.
     * @return Formatted message.
     */
    public static String formatString(ArrayList<String> messages, boolean hasHeader) {
        String formattedString = "";
        if (hasHeader) {
            formattedString = generatesHeader(messages.get(0));

            for (int i = 1; i < messages.size(); ++i) {
                formattedString = formattedString.concat(encloseRow(messages.get(i)));
            }
        } else {
            for (String s : messages) {
                formattedString = formattedString.concat(encloseRow(s));
            }
        }
        return encloseTopAndBottom(formattedString);
    }

    /**
     * Formats a array of strings to be printed out. Each element in the list will be printed in a newline.
     *
     * @param messages  Array of strings to be formatted.
     * @param hasHeader Determines if there is a header. Header MUST be the first element in the list.
     * @return Formatted message.
     */
    public static String formatString(String[] messages, boolean hasHeader) {
        String formattedString = "";
        if (hasHeader) {
            formattedString = generatesHeader(messages[0]);

            for (int i = 1; i < messages.length; ++i) {
                formattedString = formattedString.concat(encloseRow(messages[i]));
            }
        } else {
            for (String s : messages) {
                formattedString = formattedString.concat(encloseRow(s));
            }
        }
        return encloseTopAndBottom(formattedString);
    }

    /**
     * Generates a header row with the format.
     *
     * @param header Header message
     * @return Formatted header.
     */
    private static String generatesHeader(String header) {
        return encloseRow(header) + generatesRowSplit();
    }

    /**
     * Generates a row of pre-defined characters as to segregate row contents.
     *
     * @return A row of defined characters.
     */
    private static String generatesRowSplit() {
        return ROW_SPLIT.repeat(MAX_ROW_LENGTH) + LS;
    }

    /**
     * Encloses the top and bottom of the formatted message.
     *
     * @param message Formatted message to be enclosed.
     * @return Enclosed message.
     */
    private static String encloseTopAndBottom(String message) {
        return generatesRowSplit() + message + generatesRowSplit();
    }

    /**
     * Encloses the sides of the message.
     *
     * @param message Message to be enclosed.
     * @return Enclosed message.
     */
    private static String encloseRow(String message) {
        int numBlanks;

        // Array list to store startIndex, endIndex and the color of the string
        ArrayList<Integer> coloredStringStartIndexList = new ArrayList<>();
        ArrayList<Integer> coloredStringEndIndexList = new ArrayList<>();
        ArrayList<String> stringColorList = new ArrayList<>();

        int numAsciiCode = getNumAsciiCode(message, coloredStringStartIndexList, coloredStringEndIndexList,
                stringColorList);

        // Calculate the number of space needed to fill up if the message length is less than the MAX_MESSAGE_LENGTH
        numBlanks = MAX_MESSAGE_LENGTH - message.length() + numAsciiCode;

        // Adds empty space to the message
        if (numBlanks >= 0) {
            return COLUMN_START + message + EMPTY_STRING.repeat(numBlanks) + COLUMN_END + LS;
        } else {
            int startIndexOfNextLine = MAX_MESSAGE_LENGTH;
            boolean cutOffWordIsColored = false;
            String truncatedColor = RESET;

            // Checks if the start index for the next line lines between any colored word.
            for (int i = 0; i < coloredStringStartIndexList.size(); ++i) {
                // If the message contains any ansi escape code, shift the start index to print more letters since they
                // will be removed when printing
                if (startIndexOfNextLine > coloredStringStartIndexList.get(i)
                        && startIndexOfNextLine > coloredStringEndIndexList.get(i)) {
                    startIndexOfNextLine += ANSI_PREFIX_LENGTH + RESET.length();
                } else if (startIndexOfNextLine > coloredStringStartIndexList.get(i)
                        && startIndexOfNextLine <= coloredStringEndIndexList.get(i)) {
                    startIndexOfNextLine += ANSI_PREFIX_LENGTH;
                    cutOffWordIsColored = true;
                    truncatedColor = stringColorList.get(i);
                    break;
                }
            }

            String preservedMessage;
            String truncatedMessage;
            // If the cutoff is in the middle of a colored word, shift all the way to the space directly before it.
            if (cutOffWordIsColored) {
                while (message.charAt(startIndexOfNextLine) != EMPTY_CHAR) {
                    --startIndexOfNextLine;
                }
                // Split the strings and enclose individual string
                // Add the color RESET to the end of the first line
                preservedMessage = message.substring(0, startIndexOfNextLine).concat(RESET);
                // Add the color to the front of the second line
                truncatedMessage = truncatedColor + message.substring(startIndexOfNextLine);
            } else {
                // Split the strings and enclose individual string
                preservedMessage = message.substring(0, startIndexOfNextLine);
                truncatedMessage = message.substring(startIndexOfNextLine);
            }
            return encloseRow(preservedMessage) + encloseRow(truncatedMessage);
        }
    }

    /**
     * Returns the number of ascii code that appear within the message. Also match the start and end of index of each
     * color.
     *
     * @param message                     String of the message to check.
     * @param coloredStringStartIndexList Arraylist of index to mark the start of a color.
     * @param coloredStringEndIndexList   Arraylist of index to mark the end of a color.
     * @param stringColorList             Arraylist of string to store the color present.
     * @return Number of Ascii code that appeared.
     */
    private static int getNumAsciiCode(String message,
                                       ArrayList<Integer> coloredStringStartIndexList,
                                       ArrayList<Integer> coloredStringEndIndexList,
                                       ArrayList<String> stringColorList) {
        int numAsciiCode = 0;

        // Stack is used to match the PREFIX and POSTFIX of each color. Possible patterns:
        // 1. Individual string has its own color.
        // [PREFIX]string[POSTFIX] [PREFIX]string[POSTFIX]
        // 2. Individual string color has its own color but is enclosed by another color.
        // [PREFIX][PREFIX]string[POSTFIX] [PREFIX]string[POSTFIX][POSTFIX]
        // 3. String enclosed by nested colors
        // [PREFIX][PREFIX]PREFIX]string[POSTFIX][POSTFIX][POSTFIX]
        Stack<Integer> coloredStringStartIndexStack = new Stack<>();
        Stack<String> stringColorStack = new Stack<>();

        // Count the number of colored string in the message.
        String[] temp = message.split(EMPTY_STRING);
        int messageLength = 0;
        for (String s : temp) {
            // Check if it contains the RESET color
            if (s.contains(RESET)) {
                String checkString = s;
                String stringBeforeReset;
                String stringWithReset;
                int cutOffIndex = 0;

                // While it contains RESET, trim it out
                while (checkString.contains(RESET)) {
                    int resetIndex = checkString.indexOf(RESET);

                    // If there are string before the RESET
                    if (resetIndex > 0) {
                        stringBeforeReset = checkString.substring(0, resetIndex - 1);
                        stringWithReset = checkString.substring(resetIndex);
                    } else {
                        stringBeforeReset = "";
                        stringWithReset = checkString;
                    }

                    cutOffIndex += stringBeforeReset.length();

                    // If there is a color string that is not closed
                    if (stringBeforeReset.contains(PREFIX)) {
                        numAsciiCode += ANSI_PREFIX_LENGTH;
                        // Directly add to the list
                        coloredStringStartIndexList.add(messageLength);
                        stringColorList.add(checkString.substring(0, checkString.indexOf(POSTFIX) + 1));
                    } else if (coloredStringStartIndexStack.size() > 0) {
                        // Match it with the latest color in the stack
                        coloredStringStartIndexList.add(coloredStringStartIndexStack.pop());
                        stringColorList.add(stringColorStack.pop());
                    }
                    numAsciiCode += RESET.length();
                    coloredStringEndIndexList.add(messageLength + cutOffIndex);
                    checkString = stringWithReset.substring(RESET.length());
                }
            } else if (s.contains(PREFIX) && !s.contains(RESET)) { // is an ansi defined color
                numAsciiCode += ANSI_PREFIX_LENGTH;
                // Add them into the stack first
                coloredStringStartIndexStack.push(messageLength);
                stringColorStack.push(s.substring(0, s.indexOf(POSTFIX) + 1));
            }
            // Increment the message length by each string array, also account for the space
            messageLength += s.length() + 1;
        }
        return numAsciiCode;
    }
}
