package seedu.duke.util;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands.
 */
public class PrefixSyntax {

    /** Prefix definitions. */
    public static final String PREFIX_DELIMITER = "/";

    /** Shared prefix. */
    public static final String PREFIX_TITLE = "t";
    public static final String PREFIX_INDEX = "i";

    /** Note related prefix. */
    public static final String PREFIX_TAG = "tag";
    public static final String PREFIX_PIN = "pin";
    public static final String PREFIX_CONTENT = "c";
    public static final String PREFIX_LINE = "ln";
    public static final String PREFIX_SORT = "sort";
    public static final String PREFIX_ARCHIVE = "archive";

    /** Event related prefix. */
    public static final String PREFIX_TIMING = "timing";
    public static final String PREFIX_RECURRING = "repeat";
    public static final String PREFIX_REMIND = "remind";
    public static final String PREFIX_STOP_RECURRING = "stop";
    public static final String PREFIX_DATETIME = "d";

    public static final String STRING_SPLIT_DELIMITER = " ";
    public static final String STRING_SORT_ASCENDING = "up";
    public static final String STRING_SORT_DESCENDING = "down";
    public static final String SUFFIX_INDEX = ". ";
    public static final String TIMING_SPLIT_DELIMITER = "-";

    /** Note content related prefix. */
    public static final String STRING_NEW_LINE = "\n";
    public static final String PREFIX_END = "end";
    public static final String PREFIX_DELETE_LINE = "del";
}
