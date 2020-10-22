package seedu.duke.storage;

import org.junit.jupiter.api.Test;
import seedu.duke.data.exception.SystemException;
import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.Notebook;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

class StorageManagerTest {

    @Test
    void saveNotebook() throws SystemException {
        Notebook notes = new Notebook();
        String content = "Hello this is a test note\n"
                + "With a few lines\n"
                + "\n"
                + "\n"
                + "This is the end of the note\n";
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
        String content = "Hello this is a test note\n"
                        + "With a few lines\n"
                        + "\n"
                        + "\n"
                        + "This is the end of the note\n";
        Note note = new Note("TestNote", content, false, false);
        try {
            StorageManager.saveNoteContent(note);
        } catch (IOException e) {
            System.out.println("Unable to create file");
        }
    }
}
