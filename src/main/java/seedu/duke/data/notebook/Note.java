package seedu.duke.data.notebook;

import java.util.ArrayList;

/**
 * Represents a Note. Contains all the information of a note.
 */
public class Note {

    private String title;
    private String content;
    private Boolean isPinned;
    private boolean isArchived;
    private ArrayList<Tag> tags;

    /**
     * Constructs a Note object with its title, content and pinned status provided.
     *
     * @param title of the note.
     * @param content of the note.
     * @param isPinned status of the note.
     */
    public Note(String title, String content, Boolean isPinned, boolean isArchived) {
        this.title = title;
        this.content = content;
        this.isPinned = isPinned;
        this.isArchived = isArchived;
        tags = new ArrayList<>();
    }

    /**
     * Constructs a Note object with its title, content, pinned status and tags provided.
     *
     * @param title of the note.
     * @param content of the note.
     * @param isPinned status of the note.
     * @param tags of the note.
     */
    public Note(String title, String content, Boolean isPinned, boolean isArchived, ArrayList<Tag> tags) {
        this(title, content, isPinned, isArchived);
        this.tags = tags;
    }

    /**
     * Gets the title of note from existing data.
     *
     * @return title of the note.
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getPinned() {
        return (isPinned);
    }

    public String getPinnedString() {
        return (isPinned ? "Y" : "N");
    }

    public void togglePinned() {
        isPinned = !isPinned;
    }

    public void setPinned(Boolean pinned) {
        isPinned = pinned;
    }

    public ArrayList<Tag> getTags() {
        return this.tags;
    }

    public String getTagsName() {
        String tagsName = "";

        for (Tag t : tags) {
            tagsName = tagsName.concat(t.toString());
        }
        return tagsName;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}
