package seedu.notus.data.notebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Represents a TagManager. Manages the tags for the notes.
 */
public class TagManager {
    private static final Logger LOGGER = Logger.getLogger("TagManager");

    private Map<Tag, ArrayList<Note>> tagMap;

    public TagManager() {
        setupLogger();
        tagMap = new HashMap<>();
    }

    public Map<Tag, ArrayList<Note>> getTagMap() {
        return tagMap;
    }

    /**
     * Returns the Tag that matches the tag name.
     *
     * @param tagName Name of the Tag to check.
     * @return The tag if it exists, null otherwise.
     */
    public Tag getTag(String tagName) {
        for (Tag t : tagMap.keySet()) {
            if (t.getTagName().equalsIgnoreCase(tagName)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Creates a Tag with the provided Tag.
     *
     * @param tag Provided Tag.
     * @param overridesColor Determine if the tag color needs to be override.
     * @return True if new Tag is created, false otherwise.
     */
    public boolean createTag(Tag tag, boolean overridesColor) {
        // Check if there exist a tag with the same tag name.
        Tag existingTag = getTag(tag.getTagName());

        // If the tag does not exist, creates it.
        if (existingTag == null) {
            LOGGER.log(Level.INFO, "Creating a new tag: " + tag.getTagName());
            tagMap.put(tag, new ArrayList<>());
            return true;
        } else {
            if (overridesColor) {
                LOGGER.log(Level.INFO, "Overriding an existing tag: " + existingTag.getTagName());
                existingTag.setTagAttribute(tag.getTagAttribute());
            }
            return false;
        }
    }

    /**
     * Handles creation of multiple tags and returns the result of each creation.
     *
     * @param tags List of tags to be created.
     * @param createSuccessfulString String for successful creation of tag.
     * @param createUnsuccessfulString String for unsuccessful creation of tag
     * @return Result of all tag creation.
     */
    public ArrayList<String> createTag(ArrayList<Tag> tags, String createSuccessfulString,
                                   String createUnsuccessfulString) {
        ArrayList<String> result = new ArrayList<>();
        for (Tag t : tags) {
            if (createTag(t, true)) {
                result.add(createSuccessfulString + getTag(t.getTagName()));
            } else {
                result.add(createUnsuccessfulString + getTag(t.getTagName()));
            }
        }
        return result;
    }

    /**
     * Tags a Note with the provided Tag.
     *
     * @param note Note to be tagged.
     * @param tag Provided Tag.
     */
    public void tagNote(Note note, Tag tag) {
        LOGGER.log(Level.INFO, "Adding tag to note: " + tag.getTagName());
        tagMap.get(tag).add(note);
        note.getTags().add(tag);
    }

    /**
     * Removes a Tag from the Note.
     *
     * @param note Note to be untagged.
     * @param tag Tag to be removed.
     */
    public void removeTag(Note note, Tag tag) {
        LOGGER.log(Level.INFO, "Removing tag from note: " + tag.getTagName());
        tagMap.get(tag).remove(note);
        note.getTags().remove(tag);
    }

    /**
     * Deletes a Tag from the Map. Notes that have the Tag will be untagged.
     *
     * @param tag Tag to be deleted.
     * @return True if there exist the tag and is deleted, false otherwise.
     */
    public boolean deleteTag(Tag tag) {
        Tag existingTag = getTag(tag.getTagName());

        if (existingTag == null) {
            LOGGER.log(Level.INFO, "Tag does not exists, unable to delete: " + tag.getTagName());
            return false;
        }

        for (Note n : tagMap.get(existingTag)) {
            n.getTags().remove(existingTag);
        }
        LOGGER.log(Level.INFO, "Delete tag: " + tag.getTagName());
        tagMap.remove(existingTag);
        return true;
    }

    /**
     * Handles deletion of multiple tags and returns the result of each deletion.
     *
     * @param tags List of tags to be created.
     * @param deleteSuccessfulString String for successful deletion of tag.
     * @param deleteUnsuccessfulString String for unsuccessful deletion of tag.
     * @return Result of all tag creation.
     */
    public ArrayList<String> deleteTag(ArrayList<Tag> tags, String deleteSuccessfulString,
                                   String deleteUnsuccessfulString) {
        ArrayList<String> result = new ArrayList<>();
        for (Tag t : tags) {
            Tag existingTag = getTag(t.getTagName());
            if (deleteTag(t)) {
                result.add(deleteSuccessfulString + existingTag);
            } else {
                result.add(deleteUnsuccessfulString + t);
            }
        }
        return result;
    }

    /**
     * Returns an arrayList of existing tags' name in the map.
     *
     * @return ArrayList of existing tags' name.
     */
    public ArrayList<String> getAllTagsName() {
        if (tagMap.isEmpty()) {
            return null;
        } else {
            ArrayList<String> result = new ArrayList<>();
            for (Tag t : tagMap.keySet()) {
                result.add(t.toString() + " ");
            }
            return result;
        }
    }

    /**
     * Rebinds all the tags in the note to the existing tags in the database.
     *
     * @param note Note to have the tags rebind.
     */
    public void rebindTags(Note note) {
        int numTagsToCheck = note.getTags().size();

        // loop through all the tags in notes
        for (int i = 0; i < numTagsToCheck; ++i) {
            // always check against the tag of the first note
            Tag tag = note.getTags().get(0);
            LOGGER.log(Level.INFO, "Attempt to match with existing tag: " + tag.getTagName());
            // check if the tag exists in the database
            Tag existingTag = getTag(tag.getTagName());
            note.getTags().remove(tag);

            if (existingTag == null) {
                LOGGER.log(Level.INFO, "Tag does not exist");
                // if the tag does not exist in the database, create the tag and tag to note
                createTag(tag, false);
                tagNote(note, tag);
            } else if (!note.getTags().contains(existingTag)) {
                tagNote(note, existingTag);
            }
        }
    }

    /**
     * Handles tagging and untagging of note with the given list of tags. If the note already has the tag, untags it,
     * else tags the note. Returns the result of each tagging and untagging operation.
     *
     * @param note Note to be tagged or untagged.
     * @param tags List of tags.
     * @param tagNoteString String for tagging of note.
     * @param untagNoteString String for untagging of note.
     * @return Result of all tagging and untagging operation.
     */
    public ArrayList<String> tagAndUntagNote(Note note, ArrayList<Tag> tags, String tagNoteString,
                                          String untagNoteString) {
        ArrayList<String> result = new ArrayList<>();

        for (Tag t : tags) {
            // Tries to get the tag from the map
            Tag existingTag = getTag(t.getTagName());

            // check if the note contains such tag
            if (note.getTags().contains(existingTag)) {
                removeTag(note, existingTag);
                result.add(untagNoteString + existingTag);
            } else {
                // runs the create tag in case existing tag is null, if it is not null, updates the tag
                createTag(t, false);
                existingTag = getTag(t.getTagName());
                tagNote(note, existingTag);
                result.add(tagNoteString + existingTag);
            }
        }
        return result;
    }

    private void setupLogger() {
        LogManager.getLogManager().reset();
        LOGGER.setLevel(Level.INFO);

        try {
            FileHandler fileHandler = new FileHandler("TagManager.log");
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.INFO);
            LOGGER.addHandler(fileHandler);
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "File logger not working.", exception);
        }

        LOGGER.log(Level.INFO, "New tagManager object created.");
    }
}