package seedu.duke.data.notebook;

import com.diogonunes.jcolor.Attribute;
import static com.diogonunes.jcolor.Ansi.colorize;

/**
 * Represents a tag with respective color bind to it.
 */
public class Tag {

    public static final String COLOR_RED_STRING = "RED";
    public static final String COLOR_GREEN_STRING = "GREEN";
    public static final String COLOR_BLUE_STRING = "BLUE";
    public static final String COLOR_YELLOW_STRING = "YELLOW";
    public static final String COLOR_PURPLE_STRING = "PURPLE";
    public static final String COLOR_CYAN_STRING = "CYAN";
    public static final String COLOR_WHITE_STRING = "WHITE";

    /**
     * Types of color.
     */
    public enum TagColor {
        COLOR_WHITE("\u001B[97m"),
        COLOR_RED("\u001B[31m"),
        COLOR_GREEN("\u001B[32m"),
        COLOR_YELLOW("\u001B[33m"),
        COLOR_BLUE("\u001B[34m"),
        COLOR_PURPLE("\u001B[35m"),
        COLOR_CYAN("\u001B[36m"),
        COLOR_RESET("\u001B[0m");

        private String color;

        TagColor(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }

    private String tagName;
    private TagColor tagColor;
    private Attribute tagAttribute;

    /**
     * Overloaded constructor to take in the color of the tag.
     *
     * @param tagName name of the tag.
     * @param tagColor color of the tag.
     */
    public Tag(String tagName, String tagColor) {
        setTagName(tagName);
        setTagColor(tagColor);
    }

    /** Getter function for the name of the tag. */
    public String getTagName() {
        return tagName;
    }

    /** Setter function for the name of the tag. */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /** Getter function for the color of the tag. */
    public TagColor getTagColor() {
        return tagColor;
    }

    /** Setter function for the color of the tag. */
    public void setTagColor(TagColor tagColor) {
        this.tagColor = tagColor;
    }

    /** Setter function for the color of the tag. */
    public void setTagColor(String tagColor) {
        switch (tagColor.toUpperCase()) {
        case COLOR_RED_STRING:
            this.tagColor = TagColor.COLOR_RED;
            this.tagAttribute = Attribute.RED_TEXT();
            break;
        case COLOR_GREEN_STRING:
            this.tagColor = TagColor.COLOR_GREEN;
            this.tagAttribute = Attribute.GREEN_TEXT();
            break;
        case COLOR_BLUE_STRING:
            this.tagColor = TagColor.COLOR_BLUE;
            this.tagAttribute = Attribute.BLUE_TEXT();
            break;
        case COLOR_YELLOW_STRING:
            this.tagColor = TagColor.COLOR_YELLOW;
            this.tagAttribute = Attribute.YELLOW_TEXT();
            break;
        case COLOR_PURPLE_STRING:
            this.tagColor = TagColor.COLOR_PURPLE;
            this.tagAttribute = Attribute.RED_TEXT();
            break;
        case COLOR_CYAN_STRING:
            this.tagColor = TagColor.COLOR_CYAN;
            this.tagAttribute = Attribute.CYAN_TEXT();
            break;
        case COLOR_WHITE_STRING:
            this.tagColor = TagColor.COLOR_WHITE;
            this.tagAttribute = Attribute.WHITE_TEXT();
            break;
        default:
            this.tagColor = TagColor.COLOR_RESET;
            this.tagAttribute = Attribute.CLEAR();
            break;
        }
    }

    /**
     * Overrides the parent class toString function to return the tag with its tag color.
     *
     * @return tagName with its respective tag color.
     */
    @Override
    public String toString() {
        return colorize("[" + tagName + "]", tagAttribute);
        //return tagColor.color + "[" + tagName + "]" + TagColor.COLOR_RESET.color;
    }
}
