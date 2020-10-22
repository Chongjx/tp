package seedu.duke.util;

import seedu.duke.command.Command;
import seedu.duke.command.AddNoteCommand;
import seedu.duke.command.AddEventCommand;
import seedu.duke.command.ArchiveNoteCommand;
import seedu.duke.command.CreateTagCommand;
import seedu.duke.command.DeleteNoteCommand;
import seedu.duke.command.DeleteEventCommand;
import seedu.duke.command.DeleteTagCommand;
import seedu.duke.command.EditNoteCommand;
import seedu.duke.command.EditEventCommand;
import seedu.duke.command.ExitCommand;
import seedu.duke.command.FindCommand;
import seedu.duke.command.HelpCommand;
import seedu.duke.command.IncorrectCommand;
import seedu.duke.command.ListNoteCommand;
import seedu.duke.command.ListEventCommand;
import seedu.duke.command.ListTagCommand;
import seedu.duke.command.PinCommand;
import seedu.duke.command.RemindCommand;
import seedu.duke.command.TagCommand;
import seedu.duke.command.UnarchiveNoteCommand;
import seedu.duke.command.ViewNoteCommand;

import seedu.duke.data.exception.SystemException;
import seedu.duke.data.exception.SystemException.ExceptionType;
import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.Tag;
import seedu.duke.data.timetable.Event;
import seedu.duke.data.timetable.RecurringEvent;
import seedu.duke.data.timetable.DailyEvent;
import seedu.duke.data.timetable.WeeklyEvent;
import seedu.duke.data.timetable.MonthlyEvent;
import seedu.duke.data.timetable.YearlyEvent;

import static seedu.duke.util.PrefixSyntax.PREFIX_ARCHIVE;
import static seedu.duke.util.PrefixSyntax.PREFIX_DELIMITER;
import static seedu.duke.util.PrefixSyntax.PREFIX_END;
import static seedu.duke.util.PrefixSyntax.PREFIX_DELETE_LINE;
import static seedu.duke.util.PrefixSyntax.PREFIX_RECURRING;
import static seedu.duke.util.PrefixSyntax.PREFIX_REMIND;
import static seedu.duke.util.PrefixSyntax.PREFIX_STOP_RECURRING;
import static seedu.duke.util.PrefixSyntax.STRING_NEW_LINE;
import static seedu.duke.util.PrefixSyntax.PREFIX_INDEX;
import static seedu.duke.util.PrefixSyntax.PREFIX_PIN;
import static seedu.duke.util.PrefixSyntax.PREFIX_TAG;
import static seedu.duke.util.PrefixSyntax.PREFIX_TIMING;
import static seedu.duke.util.PrefixSyntax.PREFIX_TITLE;
import static seedu.duke.util.PrefixSyntax.PREFIX_SORT;
import static seedu.duke.util.PrefixSyntax.STRING_SPLIT_DELIMITER;
import static seedu.duke.util.PrefixSyntax.STRING_SORT_ASCENDING;
import static seedu.duke.util.PrefixSyntax.STRING_SORT_DESCENDING;
import static seedu.duke.util.PrefixSyntax.TIMING_SPLIT_DELIMITER;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Parses user input.
 */
public class Parser {

    private static final int CONTAINS_TAG_COLOR_INFO = 2;
    private static final int NULL_INDEX = 0;

    /**
     * Parses userInput string into a Command to be executed.
     *
     * @param userInput Original string of the userInput.
     * @return Command to be executed.
     */
    public Command parseCommand(String userInput) {

        String[] userCommandAndArguments = userInput.split(" ", 2);
        String commandString = userCommandAndArguments[0];
        String userMessage;

        try {
            userMessage = userCommandAndArguments[1];
        } catch (ArrayIndexOutOfBoundsException exception) {
            userMessage = null;
        }
        
        try {
            switch (commandString.toLowerCase()) {
            case AddNoteCommand.COMMAND_WORD:
                return prepareAddNote(userMessage);
            case AddEventCommand.COMMAND_WORD:
                return prepareAddEvent(userMessage);
            case ArchiveNoteCommand.COMMAND_WORD:
                return prepareArchiveNote(userMessage);
            case UnarchiveNoteCommand.COMMAND_WORD:
                return prepareUnarchiveNote(userMessage);
            case ListNoteCommand.COMMAND_WORD:
                return prepareListNote(userMessage);
            case ListEventCommand.COMMAND_WORD:
                return prepareListEvent(userMessage);
            case ViewNoteCommand.COMMAND_WORD:
                return prepareViewNote(userMessage);
                // return prepareViewNote(userMessage);
            case EditNoteCommand.COMMAND_WORD:
                // return prepareEditNote(userMessage);
            case EditEventCommand.COMMAND_WORD:
                // return prepareEditEvent(userMessage);
            case DeleteNoteCommand.COMMAND_WORD:
                return prepareDeleteNote(userMessage);
            case DeleteEventCommand.COMMAND_WORD:
                return prepareDeleteEvent(userMessage);
            case FindCommand.COMMAND_WORD:
                return prepareFind(userMessage);
            case PinCommand.COMMAND_WORD:
                return preparePin(userMessage);
            case CreateTagCommand.COMMAND_WORD:
                return prepareCreateOrDeleteTag(userMessage, true);
            case DeleteTagCommand.COMMAND_WORD:
                return prepareCreateOrDeleteTag(userMessage, false);
            case ListTagCommand.COMMAND_WORD:
                return new ListTagCommand();
            case TagCommand.COMMAND_WORD:
                return prepareTag(userMessage);
            case RemindCommand.COMMAND_WORD:
                return new RemindCommand();
            case ExitCommand.COMMAND_WORD:
                return new ExitCommand();
            case HelpCommand.COMMAND_WORD:
                return new HelpCommand();
            default:
                throw new SystemException(ExceptionType.EXCEPTION_INVALID_COMMAND);
            }
        } catch (SystemException exception) {
            return new IncorrectCommand(exception.getMessage());
        }
    }

    /**
     * Splits the userMessage into the respective info by the delimiter.
     *
     * @param userMessage Original string of the user message.
     * @return Split strings.
     * @throws NullPointerException when the userMessage is empty.
     */
    private ArrayList<String[]> splitInfoDetails(String userMessage) throws SystemException {
        try {
            String[] splitMessage = userMessage.split(PREFIX_DELIMITER);
            ArrayList<String[]> splitMessageContent = new ArrayList<>();

            // Splits the prefix and the remaining content
            for (String s : splitMessage) {
                splitMessageContent.add(s.split(STRING_SPLIT_DELIMITER, 2));
            }

            // Remove the first element as it is always empty
            splitMessageContent.remove(0);
            return splitMessageContent;
        } catch (NullPointerException | IndexOutOfBoundsException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_MISSING_MESSAGE_AFTER_COMMAND);
        }
    }

    /**
     * Checks if an input string if blank. If it is, throw the provided system exception. If it is not, return that
     * string trimmed.
     *
     * @param input Input to be checked.
     * @param exceptionType ExceptionType to be thrown.
     * @return Trimmed non-blank string.
     * @throws SystemException Occurs when input is blank.
     */
    private String checkBlank(String input, SystemException.ExceptionType exceptionType) throws SystemException {
        if (input.isBlank()) {
            throw new SystemException(exceptionType);
        } else {
            return input.trim();
        }
    }

    /**
     * Creates and returns a Tag object based on the info provided.
     *
     * @param tagMessage info of the Tag. Contains tag name and may contain tag color.
     * @return new Tag object.
     * @throws SystemException for missing tag name.
     */
    private Tag handleTagPrefix(String[] tagMessage) throws SystemException {
        String tagName;
        String tagColor = "";
        String tagsInfo;

        // Ensures that the message is not blank.
        try {
            tagsInfo = checkBlank(tagMessage[1], ExceptionType.EXCEPTION_MISSING_TAG);
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_MISSING_TAG);
        }

        // Split into the tag name and tag color.
        String[] tagInfo = tagsInfo.split(STRING_SPLIT_DELIMITER, 2);

        tagName = checkBlank(tagInfo[0], ExceptionType.EXCEPTION_MISSING_TAG);

        if (tagInfo.length == CONTAINS_TAG_COLOR_INFO) {
            tagColor = tagInfo[1].trim();
        }
        return new Tag(tagName, tagColor);
    }

    /**
     * Prepare userInput into Note before adding into Notebook.
     *
     * @param userMessage Original string user inputs.
     * @return Result of the add note command.
     * @throws SystemException if an error occurs.
     */
    private Command prepareAddNote(String userMessage) throws SystemException {
        Note note;
        String title = "";
        ArrayList<String> content = new ArrayList<>();
        boolean isPinned = false;
        boolean isArchived = false;
        ArrayList<Tag> tags = new ArrayList<>();

        try {
            // Get prefix
            ArrayList<String[]> splitInfo = splitInfoDetails(userMessage);

            for (String[] infoDetails : splitInfo) {
                String prefix = infoDetails[0].toLowerCase();
                switch (prefix) {
                case PREFIX_TITLE:
                    title = checkBlank(infoDetails[1], ExceptionType.EXCEPTION_MISSING_TITLE);
                    break;
                case PREFIX_TAG:
                    Tag tag = handleTagPrefix(infoDetails);
                    tags.add(tag);
                    break;
                case PREFIX_PIN:
                    isPinned = Boolean.parseBoolean(checkBlank(infoDetails[1], ExceptionType.EXCEPTION_MISSING_PIN));
                    break;
                default:
                    throw new SystemException(ExceptionType.EXCEPTION_INVALID_PREFIX);
                }
            }

            title = checkBlank(title, ExceptionType.EXCEPTION_MISSING_TITLE_PREFIX);

            // Add to note
            note = tags.isEmpty() ? new Note(title, content, isPinned, isArchived) :
                    new Note(title, content, isPinned, isArchived, tags);

            return new AddNoteCommand(note);
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_MISSING_TITLE);
        }
    }

    /**
     * Takes a user string designated to add an event and prepares it by extracting relevant information from the
     * provided required and optional tags. It requires a title tag and a timing tag (/t and /timing). Other tags allow
     * for it to be recurring and to set reminders of the event.
     * Takes the format "add-e /t {Title} /timing {YYYY-MM-DD HH:MM} [/rem [How much earlier to remind]]
     * [/rec {How often to re-occur}] [/stop {YYYY-MM-DD HH:MM}]
     *
     * @param userMessage User input message.
     * @return Returns an AddEventCommand to be executed by Duke.
     * @throws SystemException Information provided by the tags are blank, wrong or do not have a default value.
     */
    private Command prepareAddEvent(String userMessage) throws SystemException {
        // add-e eventTitle /t timing /rec occurrence /rem time before (default same day)
        String title = "";
        LocalDateTime dateTime = null;
        LocalDateTime recurringEndTime = null;
        boolean toRemind = false;
        boolean isRecurring = false;
        String recurringType = "";
        ArrayList<Integer> timePeriods = new ArrayList<>();
        ArrayList<String> timeUnits = new ArrayList<>();

        try {
            ArrayList<String[]> splitInfo = splitInfoDetails(userMessage);
            for (String[] infoDetails : splitInfo) {
                String prefix = infoDetails[0].toLowerCase();
                ExceptionType exception;
                switch (prefix) {
                case PREFIX_TITLE:
                    exception = ExceptionType.EXCEPTION_MISSING_TITLE;
                    title = checkBlank(infoDetails[1], exception);
                    break;
                case PREFIX_TIMING:
                    exception = ExceptionType.EXCEPTION_MISSING_TIMING;
                    String timingString = checkBlank(infoDetails[1], exception);
                    dateTime = DateTimeManager.dateTimeParser(timingString);
                    break;
                case PREFIX_REMIND:
                    toRemind = true;
                    if (infoDetails.length == 1 || infoDetails[1].isBlank()) {
                        timeUnits.add(Event.REMINDER_DAY);
                        timePeriods.add(1);
                        break;
                    }
                    String[] reminderDetails = infoDetails[1].split(STRING_SPLIT_DELIMITER);
                    for (String reminderDetail : reminderDetails) {
                        String[] timeStrings = reminderDetail.split(TIMING_SPLIT_DELIMITER);
                        if (timeStrings.length != 2) {
                            throw new SystemException(ExceptionType.EXCEPTION_INVALID_REMINDER_FORMAT);
                        }
                        int timePeriod;
                        String timeUnit;
                        try {
                            timePeriod = Integer.parseInt(timeStrings[0]);
                            timeUnit = timeStrings[1];
                            if ((!timeUnit.equalsIgnoreCase(Event.REMINDER_DAY)
                                    && !timeUnit.equalsIgnoreCase(Event.REMINDER_WEEK)) || timePeriod < 1) {
                                throw new SystemException(ExceptionType.EXCEPTION_INVALID_REMINDER_FORMAT);
                            }

                            exception = ExceptionType.EXCEPTION_EARLY_REMINDER;
                            if (timeUnit.equalsIgnoreCase(Event.REMINDER_WEEK) && timePeriod > 1) {
                                throw new SystemException(exception);
                            } else if (timeUnit.equalsIgnoreCase(Event.REMINDER_DAY) && timePeriod > 7) {
                                throw new SystemException(exception);
                            }
                        } catch (NumberFormatException exceptionNumFormat) {
                            throw new SystemException(ExceptionType.EXCEPTION_INVALID_REMINDER_FORMAT);
                        }
                        timeUnits.add(timeUnit);
                        timePeriods.add(timePeriod);
                    }
                    break;
                case PREFIX_RECURRING:
                    isRecurring = true;
                    exception = ExceptionType.EXCEPTION_MISSING_RECURRING_TYPE;
                    try {
                        recurringType = checkBlank(infoDetails[1], exception).toLowerCase();
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        recurringType = RecurringEvent.DAILY_RECURRENCE_TYPE;
                    }
                    break;
                case PREFIX_STOP_RECURRING:
                    exception = ExceptionType.EXCEPTION_MISSING_RECURRING_END_TIME;
                    String endTimingString = checkBlank(infoDetails[1], exception);
                    recurringEndTime = DateTimeManager.dateTimeParser(endTimingString);
                    break;
                default:
                    throw new SystemException(SystemException.ExceptionType.EXCEPTION_INVALID_PREFIX);
                }
            }
            title = checkBlank(title, ExceptionType.EXCEPTION_MISSING_TITLE);

            if (dateTime == null) {
                throw new SystemException(SystemException.ExceptionType.EXCEPTION_MISSING_TIMING);
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_MISSING_DESCRIPTION);
        }

        Event event;

        if (isRecurring) {
            LocalDate date = (recurringEndTime != null) ? recurringEndTime.toLocalDate() : null;
            switch (recurringType) {
            case RecurringEvent.DAILY_RECURRENCE_TYPE:
                event = new DailyEvent(title, dateTime, toRemind, date, timePeriods, timeUnits);
                break;
            case RecurringEvent.WEEKLY_RECURRENCE_TYPE:
                event = new WeeklyEvent(title, dateTime, toRemind, date, timePeriods, timeUnits);
                break;
            case RecurringEvent.MONTHLY_RECURRENCE_TYPE:
                event = new MonthlyEvent(title, dateTime, toRemind, date, timePeriods, timeUnits);
                break;
            case RecurringEvent.YEARLY_RECURRENCE_TYPE:
                event = new YearlyEvent(title, dateTime, toRemind, date, timePeriods, timeUnits);
                break;
            default:
                throw new SystemException(ExceptionType.EXCEPTION_INVALID_RECURRING_TYPE);
            }
        } else {
            event = new Event(title, dateTime, toRemind, false, timePeriods, timeUnits);
        }
        return new AddEventCommand(event);
    }

    /**
     * Used for input of note content and processing the input into a readable data.
     *
     * @return A string of converted content input.
     * @throws StringIndexOutOfBoundsException if an error occurs.
     */
    public static ArrayList<String> inputContent() {
        boolean isInputSuccess = false;
        ArrayList<String> inputString;

        do {
            Scanner input = new Scanner(System.in);
            inputString = new ArrayList<>();

            System.out.println("Enter Note:");
            System.out.println("Type /del to delete your previous line");
            System.out.println("Type /end on a new line to end your note input");
            try {
                // Type note
                do {
                    inputString.add(input.nextLine());

                    // "/del" Delete previous line if there user makes a typo
                    if (inputString.get(inputString.size() - 1)
                            .equalsIgnoreCase(PREFIX_DELIMITER + PREFIX_DELETE_LINE)) {
                        inputString.remove(inputString.size() - 1);
                        inputString.remove(inputString.size() - 1);
                    }
                } while (!inputString.get(inputString.size() - 1)
                        .equalsIgnoreCase(PREFIX_DELIMITER + PREFIX_END)); // "/end" to end input note

                // Delete "/end" command when user ends the edit
                inputString.remove(inputString.size() - 1);

                if (inputString.size() != 0) {
                    isInputSuccess = true;
                } else {
                    System.out.println(SystemException.ExceptionType.EXCEPTION_CONTENT_MISSING);
                }
            } catch (StringIndexOutOfBoundsException exception) {
                System.out.println(SystemException.ExceptionType.EXCEPTION_INVALID_END_INPUT);
            }
        } while (!isInputSuccess);

        return inputString;
    }

    /**
     * Delete the last line for mistakes made in inputContent().
     *
     * @param commandInput Original string of the note content.
     * @param characters String of character to be removed.
     * @param charCount Number of character. 0 to remove new line, 1 to resume typing on the same line.
     */
    public static void deleteLine(StringBuilder commandInput, String characters, int charCount) {
        int lastChar = commandInput.lastIndexOf(characters) + charCount;
        commandInput.delete(lastChar, commandInput.length());
    }

    /**
     * Prepare userInput into a int before deletion.
     *
     * @param userMessage Original string user inputs.
     * @return Result of the delete note command.
     * @throws SystemException if an error occurs.
     */
    private Command prepareDeleteNote(String userMessage) throws SystemException {
        int index;
        String title;
        String prefix;
        boolean isIndex = false;

        try {
            // Get prefix
            ArrayList<String[]> splitInfo = splitInfoDetails(userMessage);

            for (String[] infoDetails : splitInfo) {
                prefix = infoDetails[0].toLowerCase();
                switch (prefix) {
                case PREFIX_INDEX:
                    isIndex = true;
                    index = Integer.parseInt(checkBlank(infoDetails[1], ExceptionType.EXCEPTION_MISSING_INDEX));

                    if (index <= NULL_INDEX) {
                        throw new SystemException(ExceptionType.EXCEPTION_INVALID_INDEX_VALUE);
                    }
                    return new DeleteNoteCommand(index - 1);
                case PREFIX_TITLE:
                    title = checkBlank(infoDetails[1], ExceptionType.EXCEPTION_MISSING_TITLE);
                    return new DeleteNoteCommand(title);
                default:
                    throw new SystemException(ExceptionType.EXCEPTION_INVALID_PREFIX);
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            if (isIndex) {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_INDEX);
            } else {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_TITLE);
            }
        } catch (NumberFormatException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_INVALID_INDEX_FORMAT);
        }
        throw new SystemException(ExceptionType.EXCEPTION_INVALID_INPUT_FORMAT);
    }

    /**
     * Prepare userInput into a int before archiving.
     *
     * @param userMessage Original string user inputs.
     * @return Result of the archived note command.
     * @throws SystemException if an error occurs.
     */
    private Command prepareArchiveNote(String userMessage) throws SystemException {
        int index;
        String title;
        String prefix;
        boolean isIndex = false;

        try {
            // Get prefix
            ArrayList<String[]> splitInfo = splitInfoDetails(userMessage);

            for (String[] infoDetails : splitInfo) {
                prefix = infoDetails[0].toLowerCase();
                switch (prefix) {
                case PREFIX_INDEX:
                    isIndex = true;
                    index = Integer.parseInt(checkBlank(infoDetails[1], ExceptionType.EXCEPTION_MISSING_INDEX));

                    if (index <= NULL_INDEX) {
                        throw new SystemException(ExceptionType.EXCEPTION_INVALID_INDEX_VALUE);
                    }
                    return new ArchiveNoteCommand(index - 1);
                case PREFIX_TITLE:
                    title = checkBlank(infoDetails[1], ExceptionType.EXCEPTION_MISSING_TITLE);
                    return new ArchiveNoteCommand(title);
                default:
                    throw new SystemException(ExceptionType.EXCEPTION_INVALID_PREFIX);
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            if (isIndex) {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_INDEX);
            } else {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_TITLE);
            }
        } catch (NumberFormatException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_INVALID_INDEX_FORMAT);
        }
        throw new SystemException(ExceptionType.EXCEPTION_INVALID_INPUT_FORMAT);
    }

    /**
     * Prepare userInput into a int before un-archiving.
     *
     * @param userMessage Original string user inputs.
     * @return Result of the un-archived note command.
     * @throws SystemException if an error occurs.
     */
    private Command prepareUnarchiveNote(String userMessage) throws SystemException {
        int index;
        String title;
        String prefix;
        boolean isIndex = false;

        try {
            // Get prefix
            ArrayList<String[]> splitInfo = splitInfoDetails(userMessage);

            for (String[] infoDetails : splitInfo) {
                prefix = infoDetails[0].toLowerCase();
                switch (prefix) {
                case PREFIX_INDEX:
                    isIndex = true;
                    index = Integer.parseInt(checkBlank(infoDetails[1], ExceptionType.EXCEPTION_MISSING_INDEX));

                    if (index <= NULL_INDEX) {
                        throw new SystemException(ExceptionType.EXCEPTION_INVALID_INDEX_VALUE);
                    }
                    return new UnarchiveNoteCommand(index - 1);
                case PREFIX_TITLE:
                    title = checkBlank(infoDetails[1], ExceptionType.EXCEPTION_MISSING_TITLE);
                    return new UnarchiveNoteCommand(title);
                default:
                    throw new SystemException(ExceptionType.EXCEPTION_INVALID_PREFIX);
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            if (isIndex) {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_INDEX);
            } else {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_TITLE);
            }
        } catch (NumberFormatException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_INVALID_INDEX_FORMAT);
        }
        throw new SystemException(ExceptionType.EXCEPTION_INVALID_INPUT_FORMAT);
    }



    /**
     * Ensures that the user does not leave input blank after entering the find command word.
     *
     * @param userMessage user's input of the keyword.
     * @return new FindCommand Command.
     * @throws SystemException for missing keyword.
     */
    private Command prepareFind(String userMessage) throws SystemException {
        Logger loggerFind = Logger.getLogger("ParserPrepareFind");
        setupLogger(loggerFind, "FindCommandParser.log");

        try {
            userMessage = checkBlank(userMessage, ExceptionType.EXCEPTION_MISSING_KEYWORD);
            loggerFind.log(Level.INFO, "If no null pointer, keyword is trimmed.");
        } catch (NullPointerException exception) {
            loggerFind.log(Level.INFO, "Null pointer exception caught.");
            throw new SystemException(ExceptionType.EXCEPTION_MISSING_KEYWORD);
        }
        loggerFind.log(Level.INFO, "Will execute FindCommand");
        return new FindCommand(userMessage);
    }

    /**
     * Returns a ListNote Command.
     * ListNoteCommand is overloaded, so depending on the user input i.e
     * list-n /tag TAG up/down
     * tags and up/down are optional parameters for users to input
     * up/down is to sort the list alphabetically either A-Z or Z-A
     *
     * @param userMessage user's input of the keyword.
     * @return new ListNoteCommand Command.
     */
    private Command prepareListNote(String userMessage) throws SystemException {
        // If no optional parameters, return default display of list note
        if (userMessage == null) {
            return new ListNoteCommand();
        }

        String tagName;
        String sort;
        boolean isArchive = false;
        Boolean isAscending = null;
        ArrayList<String> tagsName = new ArrayList<>();
        boolean isTag = false;

        try {
            ArrayList<String[]> splitInfo = splitInfoDetails(userMessage);

            for (String[] infoDetails : splitInfo) {
                String prefix = infoDetails[0].toLowerCase();
                ExceptionType exception;

                switch (prefix) {
                case PREFIX_TAG:
                    isTag = true;
                    exception = ExceptionType.EXCEPTION_MISSING_TAG;
                    tagName = checkBlank(infoDetails[1], exception);
                    tagsName.add(tagName);
                    break;
                case PREFIX_SORT:
                    isTag = false;
                    exception = ExceptionType.EXCEPTION_MISSING_SORT;
                    sort = checkBlank(infoDetails[1], exception);
                    if (sort.equalsIgnoreCase(STRING_SORT_ASCENDING)) {
                        isAscending = true;
                    } else if (sort.equalsIgnoreCase(STRING_SORT_DESCENDING)) {
                        isAscending = false;
                    } else {
                        throw new SystemException(ExceptionType.EXCEPTION_INVALID_SORT_TYPE);
                    }
                    break;
                case PREFIX_ARCHIVE:
                    isTag = false;
                    exception = ExceptionType.EXCEPTION_MISSING_INDEX;
                    isArchive = true;
                    break;
                default:
                    throw new SystemException(ExceptionType.EXCEPTION_INVALID_PREFIX);
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            if (isTag) {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_TAG);
            } else {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_SORT);
            }
        }

        if (isArchive) {
            return new ListNoteCommand(isArchive);
        }
        
        // No optional parameters case as it is already accounted
        // Minimally if no tag, will have up/down and vice versa
        if (tagsName.isEmpty() && isAscending == null) {
            return new ListNoteCommand();
        } else if (tagsName.isEmpty() && isAscending != null) {
            return new ListNoteCommand(isAscending);
        } else if (!tagsName.isEmpty() && isAscending == null) {
            return new ListNoteCommand(tagsName);
        } else {
            return new ListNoteCommand(isAscending, tagsName);
        }
    }

    private Command prepareListEvent(String userMessage) throws SystemException {
        if (userMessage == null) {
            return new ListEventCommand();
        } else {
            ArrayList<String[]> splitInfoDetails = splitInfoDetails(userMessage);
            String details = "";
            int year;
            int month;

            if (splitInfoDetails.size() == 0) {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_TIMING_PREFIX);
            }

            for (String[] infoDetails : splitInfoDetails) {
                String prefix = infoDetails[0].toLowerCase();
                if (PREFIX_TIMING.equalsIgnoreCase(prefix)) {
                    ExceptionType exception = ExceptionType.EXCEPTION_INVALID_LIST_TIMING_FORMAT;
                    details = checkBlank(infoDetails[1], exception);
                } else {
                    throw new SystemException(ExceptionType.EXCEPTION_INVALID_PREFIX);
                }
            }
            try {
                String[] timings = details.split(TIMING_SPLIT_DELIMITER);
                if (timings.length == 1) {
                    year = Integer.parseInt(timings[0]);
                    if (year <= 1000 || year > 3000) {
                        throw new SystemException(ExceptionType.EXCEPTION_SEARCH_DATE_OUT_OF_RANGE);
                    } else {
                        return new ListEventCommand(year);
                    }
                } else {
                    year = Integer.parseInt(timings[0]);
                    month = Integer.parseInt(timings[1]);
                }
                if (year <= ListEventCommand.SMALLEST_YEAR || year >= ListEventCommand.LARGEST_YEAR
                        || month <= ListEventCommand.SMALLEST_MONTH || month >= ListEventCommand.LARGEST_MONTH) {
                    throw new SystemException(ExceptionType.EXCEPTION_SEARCH_DATE_OUT_OF_RANGE);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
                throw new SystemException(ExceptionType.EXCEPTION_INVALID_LIST_TIMING_FORMAT);
            }
            return new ListEventCommand(year, month);
        }
    }

    private Command prepareViewNote(String userMessage) throws SystemException {
        String title;
        int index;
        boolean isTitle = false;

        try {
            ArrayList<String[]> splitInfo = splitInfoDetails(userMessage);

            for (String[] infoDetails : splitInfo) {
                String prefix = infoDetails[0].toLowerCase();
                ExceptionType exception;
                switch (prefix) {
                case PREFIX_TITLE:
                    isTitle = true;
                    exception = ExceptionType.EXCEPTION_MISSING_TITLE;
                    title = checkBlank(infoDetails[1],exception);
                    return new ViewNoteCommand(title);
                case PREFIX_INDEX:
                    isTitle = false;
                    exception = ExceptionType.EXCEPTION_MISSING_INDEX;
                    index = Integer.parseInt(checkBlank(infoDetails[1], exception));
                    return new ViewNoteCommand(index - 1);
                default:
                    throw new SystemException(ExceptionType.EXCEPTION_INVALID_PREFIX);
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            if (isTitle) {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_TITLE);
            } else {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_INDEX);
            }
        } catch (NumberFormatException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_INVALID_INDEX_FORMAT);
        }
        throw new SystemException(ExceptionType.EXCEPTION_MISSING_DESCRIPTION);
    }

    /**
     * Parses the variables in userMessage to a form that is used in DeleteEventCommand.
     * @param userMessage User Input without the action word.
     * @return Returns a DeleteEventCommand to be executed by Duke.
     * @throws SystemException When the index is not numeric (e.g. index = 1%s).
     */
    private Command prepareDeleteEvent(String userMessage) throws SystemException {
        int index;
        try {
            index = Integer.parseInt(checkBlank(userMessage, ExceptionType.EXCEPTION_MISSING_INDEX));
        } catch (NumberFormatException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_INVALID_INDEX_FORMAT);
        }

        if (index <= NULL_INDEX) {
            throw new SystemException(ExceptionType.EXCEPTION_INVALID_INDEX_VALUE);
        }

        // Convert from human-readable index to index in array.
        return new DeleteEventCommand(index - 1);
    }

    /*
    private Command prepareEditNote(String userMessage) {
       return new EditCommand(index, note);
    }

    private Command prepareEditEvent(String userMessage) {
       return new EditCommand(index, event);
    }
    */

    private Command preparePin(String userMessage) throws SystemException {
        String title;
        int index;
        boolean isTitle = false;

        try {
            ArrayList<String[]> splitInfo = splitInfoDetails(userMessage);

            for (String[] infoDetails : splitInfo) {
                String prefix = infoDetails[0].toLowerCase();
                ExceptionType exception;
                switch (prefix) {
                case PREFIX_TITLE:
                    isTitle = true;
                    exception = ExceptionType.EXCEPTION_MISSING_TITLE;
                    title = checkBlank(infoDetails[1], exception);
                    return new PinCommand(title);
                case PREFIX_INDEX:
                    isTitle = false;
                    exception = ExceptionType.EXCEPTION_MISSING_INDEX;
                    index = Integer.parseInt(checkBlank(infoDetails[1], exception));
                    return new PinCommand(index - 1);
                default:
                    throw new SystemException(ExceptionType.EXCEPTION_INVALID_PREFIX);
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            if (isTitle) {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_TITLE);
            } else {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_INDEX);
            }
        } catch (NumberFormatException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_INVALID_INDEX_FORMAT);
        }
        throw new SystemException(ExceptionType.EXCEPTION_MISSING_DESCRIPTION);
    }

    /**
     * Returns a CreateTagCommand or a DeleteTagCommand based on the user message.
     *
     * @param userMessage Original string of the user message.
     * @param isCreate determines the type of command (CreateTagCommand or DeleteTagCommand) to return.
     * @return either a new CreateTagCommand or DeleteTagCommand.
     * @throws SystemException for missing tag prefix or tag name.
     */
    private Command prepareCreateOrDeleteTag(String userMessage, boolean isCreate) throws SystemException {
        ArrayList<Tag> tags = new ArrayList<>();

        try {
            ArrayList<String[]> splitInfo = splitInfoDetails(userMessage);

            for (String[] infoDetails : splitInfo) {
                String prefix = infoDetails[0];
                if (prefix.equalsIgnoreCase(PREFIX_TAG)) {
                    Tag tag = handleTagPrefix(infoDetails);
                    tags.add(tag);
                } else {
                    throw new SystemException(ExceptionType.EXCEPTION_INVALID_PREFIX);
                }
            }
            // Ensures that there is at least 1 tag to be created or deleted.
            if (tags.isEmpty()) {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_TAG_PREFIX);
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_MISSING_TAG);
        }

        if (isCreate) {
            return new CreateTagCommand(tags);
        } else {
            return new DeleteTagCommand(tags);
        }
    }

    /**
     * Returns a TagCommand to tag a note.
     *
     * @param userMessage Original string of the user message.
     * @return a new TagCommand.
     * @throws SystemException for invalid index input, missing tag prefix or tag name.
     */
    private Command prepareTag(String userMessage) throws SystemException {
        int index = NULL_INDEX;
        ArrayList<Tag> tags = new ArrayList<>();

        try {
            ArrayList<String[]> splitInfo = splitInfoDetails(userMessage);

            for (String[] infoDetails : splitInfo) {
                String prefix = infoDetails[0].toLowerCase();
                switch (prefix) {
                case PREFIX_TAG:
                    Tag tag = handleTagPrefix(infoDetails);
                    tags.add(tag);
                    break;
                case PREFIX_INDEX:
                    ExceptionType exception = ExceptionType.EXCEPTION_MISSING_INDEX;
                    index = Integer.parseInt(checkBlank(infoDetails[1].trim(), exception));
                    break;
                default:
                    throw new SystemException(ExceptionType.EXCEPTION_INVALID_PREFIX);
                }
            }

            if (tags.isEmpty()) {
                throw new SystemException(ExceptionType.EXCEPTION_MISSING_TAG_PREFIX);
            }

            if (index <= NULL_INDEX) {
                throw new SystemException(ExceptionType.EXCEPTION_INVALID_INDEX_VALUE);
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_MISSING_INDEX);
        } catch (NumberFormatException exception) {
            throw new SystemException(ExceptionType.EXCEPTION_INVALID_INDEX_FORMAT);
        }
        return new TagCommand(index - 1, tags);
    }

    /**
     * Set Up method for logging
     * Takes in a Logger variable to ensure that separates loggers can be used
     * for the respective methods.
     * Sets up what to be printed to the console (only logs that are severe)
     * Sets up what to be printed to the file (logs that are of Level.INFO and above)
     *
     * @param logger A Logger variable to be used
     * @param logFileName Name to be given for .log file.
     */
    public void setupLogger(Logger logger, String logFileName) {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.INFO);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.SEVERE);
        logger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler(logFileName);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.INFO);
            logger.addHandler(fileHandler);
        } catch (IOException error) {
            logger.log(Level.SEVERE, "File logger not working.", error);
        }
    }

    /*
    private Command prepareRemind(String userMessage) {
       return new RemindCommand(index, isToRemind);
    }
    */
}













