package seedu.duke.storage;

import org.junit.jupiter.api.Test;
import seedu.duke.data.exception.SystemException;
import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.Notebook;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

class StorageManagerTest {

    @Test
    void saveNotebook() throws SystemException {
        Notebook notes = new Notebook();
        ArrayList<String> content = new ArrayList<>();
        content.add("Hello this is a test note");
        content.add("With a few lines");
        content.add("");
        content.add("");
        content.add("This is the end of the note");

        for (int i = 0; i < 10; i++) {
            String title = "testNote_" + i;
            Boolean isPinned = false;
            if (i % 2 == 0) {
                isPinned = true;
            }
            Note note = new Note(title, content, isPinned, false);
            notes.addNote(note);
        }
        StorageManager.saveNotebook(notes);
    }

    @Test
    void saveAll() {
    }

    @Test
    void loadAll() {
    }

    @Test
    void saveNoteContent() {
        ArrayList<String> content = new ArrayList<>();
        content.add("Hello this is a test note");
        content.add("With a few lines");
        content.add("");
        content.add("");
        content.add("This is the end of the note");

        Note note = new Note("TestNote", content, false, false);
      
        try {
            StorageManager.saveNoteContent(note);
        } catch (IOException e) {
            System.out.println("Unable to create file");
        }
    }
}
