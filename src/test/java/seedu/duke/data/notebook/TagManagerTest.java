package seedu.duke.data.notebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TagManagerTest {

    private Tag tagCS2113;
    private Tag tagImportant;
    private Tag tagSchool;
    private Tag tagPersonal;
    private Tag tagDefault;

    private Note noteCS2113;
    private Note noteJavaOop;
    private Note noteMath;
    private Note notePersonal;
    private Note noteDefault;

    private TagManager emptyTagManager;
    private TagManager defaultTagManager;

    @BeforeEach
    public void setUp() {
        tagCS2113 = new Tag("CS2113", Tag.COLOR_BLUE_STRING);
        tagImportant = new Tag("Important", Tag.COLOR_RED_STRING);
        tagSchool = new Tag("School", Tag.COLOR_YELLOW_STRING);
        tagPersonal = new Tag("Personal", Tag.COLOR_WHITE_STRING);
        tagDefault = new Tag("Default", Tag.COLOR_WHITE_STRING);

        ArrayList<String> contentOne = new ArrayList<>();
        contentOne.add("This is a fun mod!");
        ArrayList<String> contentTwo = new ArrayList<>();
        contentTwo.add("Abstraction");
        ArrayList<String> contentThree = new ArrayList<>();
        contentThree.add("1+1=0");
        ArrayList<String> contentFour = new ArrayList<>();
        contentFour.add("My name is ABC");
        ArrayList<String> contentFive = new ArrayList<>();
        contentFive.add("Default");

        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(tagCS2113);
        tags.add(tagImportant);
        tags.add(tagSchool);

        noteCS2113 = new Note("CS2113", contentOne, false, false, tags);

        noteJavaOop = new Note("Java OOP", contentTwo, false, false, tags);

        tags = new ArrayList<>();

        tags.add(tagSchool);
        tags.add(tagImportant);

        noteMath = new Note("Math Note", contentThree, false, false, tags);

        tags = new ArrayList<>();

        tags.add(tagPersonal);

        notePersonal = new Note("Personal Note", contentFour, false, false, tags);

        noteDefault = new Note("Default", contentFive, false, false);

        emptyTagManager = new TagManager();
        defaultTagManager = new TagManager();

        defaultTagManager.createTag(tagCS2113, true);
        defaultTagManager.createTag(tagImportant, true);
        defaultTagManager.createTag(tagSchool, true);
        defaultTagManager.createTag(tagPersonal, true);
        defaultTagManager.createTag(tagDefault, true);

        defaultTagManager.rebindTags(noteCS2113);
        defaultTagManager.rebindTags(noteJavaOop);
        defaultTagManager.rebindTags(noteMath);
        defaultTagManager.rebindTags(notePersonal);
    }

    @Test
    public void createTag_tagDoesNotExist_createsTagReturnTrue() {
        assertTrue(emptyTagManager.createTag(tagCS2113, true));
        assertTrue(emptyTagManager.createTag(tagImportant, true));
        assertTrue(emptyTagManager.createTag(tagSchool, true));
        assertTrue(emptyTagManager.createTag(tagPersonal, true));

        assertTrue(emptyTagManager.getTagMap().containsKey(tagCS2113));
        assertTrue(emptyTagManager.getTagMap().containsKey(tagImportant));
        assertTrue(emptyTagManager.getTagMap().containsKey(tagSchool));
        assertTrue(emptyTagManager.getTagMap().containsKey(tagPersonal));
    }

    @Test
    public void createTag_tagExist_returnFalse() {
        assertFalse(defaultTagManager.createTag(tagCS2113, false));
        assertFalse(defaultTagManager.createTag(tagImportant, false));
        assertFalse(defaultTagManager.createTag(tagSchool, false));
        assertFalse(defaultTagManager.createTag(tagPersonal, false));
    }

    @Test
    public void getTag_tagDoesNotExist_returnNull() {
        assertNull(emptyTagManager.getTag(tagCS2113.getTagName()));
        assertNull(emptyTagManager.getTag(tagImportant.getTagName()));
        assertNull(emptyTagManager.getTag(tagSchool.getTagName()));
        assertNull(emptyTagManager.getTag(tagPersonal.getTagName()));
    }

    @Test
    public void getTag_tagExist_returnTag() {
        assertEquals(defaultTagManager.getTag(tagCS2113.getTagName()), tagCS2113);
        assertEquals(defaultTagManager.getTag(tagImportant.getTagName()), tagImportant);
        assertEquals(defaultTagManager.getTag(tagSchool.getTagName()), tagSchool);
        assertEquals(defaultTagManager.getTag(tagPersonal.getTagName()), tagPersonal);
    }

    @Test
    public void tagNote_tagExist_tagNote() {
        // noteDefault does not have tagDefault at this point
        assertEquals(noteDefault.getTags().size(), 0);
        assertEquals(defaultTagManager.getTagMap().get(tagDefault).size(), 0);

        // Tags tagDefault to noteDefault
        defaultTagManager.tagNote(noteDefault, tagDefault);
        assertEquals(noteDefault.getTags().size(), 1);
        assertEquals(defaultTagManager.getTagMap().get(tagDefault).size(), 1);
        assertTrue(noteDefault.getTags().contains(tagDefault));
        assertFalse(noteDefault.getTags().contains(tagCS2113));
        assertFalse(noteDefault.getTags().contains(tagImportant));

        // Tags tagCS2113 to noteDefault
        defaultTagManager.tagNote(noteDefault, tagCS2113);
        assertEquals(noteDefault.getTags().size(), 2);
        assertEquals(defaultTagManager.getTagMap().get(tagCS2113).size(), 3);
        assertTrue(noteDefault.getTags().contains(tagDefault));
        assertTrue(noteDefault.getTags().contains(tagCS2113));
        assertFalse(noteDefault.getTags().contains(tagImportant));

        // Tags tagImportant to noteDefault
        defaultTagManager.tagNote(noteDefault, tagImportant);
        assertEquals(noteDefault.getTags().size(), 3);
        assertEquals(defaultTagManager.getTagMap().get(tagImportant).size(), 4);
        assertTrue(noteDefault.getTags().contains(tagDefault));
        assertTrue(noteDefault.getTags().contains(tagCS2113));
        assertTrue(noteDefault.getTags().contains(tagImportant));
    }

    @Test
    public void tagNote_tagDoesNotExist_createsTagAndTagNote() {
        emptyTagManager.createTag(tagDefault, true);
        emptyTagManager.tagNote(noteDefault, tagDefault);
        assertTrue(emptyTagManager.getTagMap().containsKey(tagDefault));
        assertEquals(noteDefault.getTags().size(), 1);
        assertTrue(noteDefault.getTags().contains(tagDefault));
        assertEquals(emptyTagManager.getTagMap().get(tagDefault).size(), 1);
        assertTrue(emptyTagManager.getTagMap().get(tagDefault).contains(noteDefault));
    }

    @Test
    public void removeTag_noteContainsTag_removeTag() {
        assertTrue(noteCS2113.getTags().contains(tagCS2113));
        assertTrue(defaultTagManager.getTagMap().get(tagCS2113).contains(noteCS2113));

        defaultTagManager.removeTag(noteCS2113, tagCS2113);
        assertFalse(noteCS2113.getTags().contains(tagCS2113));
        assertFalse(defaultTagManager.getTagMap().get(tagCS2113).contains(noteCS2113));
    }

    @Test
    public void deleteTag_tagDoesNotExist_returnFalse() {
        assertFalse(emptyTagManager.deleteTag(tagCS2113));
        assertFalse(emptyTagManager.deleteTag(tagImportant));
        assertFalse(emptyTagManager.deleteTag(tagSchool));
        assertFalse(emptyTagManager.deleteTag(tagPersonal));
        assertFalse(emptyTagManager.deleteTag(tagDefault));
    }

    @Test
    public void deleteTag_ragExist_deletesTagReturnTrue() {
        assertNotEquals(noteCS2113.getTags().size(), 0);
        assertNotEquals(noteJavaOop.getTags().size(), 0);
        assertNotEquals(noteMath.getTags().size(), 0);
        assertNotEquals(notePersonal.getTags().size(), 0);

        assertTrue(defaultTagManager.deleteTag(tagCS2113));
        assertTrue(defaultTagManager.deleteTag(tagImportant));
        assertTrue(defaultTagManager.deleteTag(tagSchool));
        assertTrue(defaultTagManager.deleteTag(tagPersonal));

        assertFalse(defaultTagManager.getTagMap().containsKey(tagCS2113));
        assertFalse(defaultTagManager.getTagMap().containsKey(tagImportant));
        assertFalse(defaultTagManager.getTagMap().containsKey(tagSchool));
        assertFalse(defaultTagManager.getTagMap().containsKey(tagPersonal));

        assertEquals(noteCS2113.getTags().size(), 0);
        assertEquals(noteJavaOop.getTags().size(), 0);
        assertEquals(noteMath.getTags().size(), 0);
        assertEquals(notePersonal.getTags().size(), 0);
    }

    //@Test
    //public void listTags_emptyTags_returnDefaultMessage() {
    //    assertEquals(emptyTagManager.listTags(), TagManager.STRING_TAG_EMPTY);
    //}
}