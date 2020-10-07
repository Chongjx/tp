package seedu.duke.command;

/**
 * Deletes an existing Tag.
 */
public class DeleteTagCommand extends Command {

    public static final String COMMAND_WORD = "delete-t";
    public static final String COMMAND_SUCCESSFUL_MESSAGE = "Deleted the tag!";
    public static final String COMMAND_UNSUCCESSFUL_MESSAGE = "The tag does not exist!";

    private static final String COMMAND_USAGE = COMMAND_WORD + ": Deletes a tag. Parameters: TAG_NAME";

    private String tagName;

    public static String getCommandUsage() {
        return COMMAND_USAGE;
    }

    public DeleteTagCommand(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String execute() {
        boolean executeSuccessful = tagManager.deleteTag(tagName);
        if (executeSuccessful) {
            return COMMAND_SUCCESSFUL_MESSAGE;
        } else {
            return COMMAND_UNSUCCESSFUL_MESSAGE;
        }
    }
}
