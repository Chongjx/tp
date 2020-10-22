package seedu.duke.storage;

import seedu.duke.data.exception.SystemException;
import seedu.duke.data.notebook.Note;
import seedu.duke.data.notebook.Notebook;
import seedu.duke.data.timetable.Timetable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents a StorageManager. Manages the saving and loading of task list data.
 */
public class StorageManager {
    /** Default folders directory. */
    private static final String FOLDER_DIR = "data";
    private static final String NOTES_DIR = "/notes";
    /** Default file path. */
    private static final String NOTEBOOK_FILE_PATH = "notebook.txt";
    private static final String TIMETABLE_FILE_PATH = "timetable.txt";

    private static final String DELIMITER = "|";


    /**
     * Saves all the Notes in the Notebook to the storage file.
     *
     * @param notebook The Notebook containing all the notes to be saved.
     */
    public static void saveNotebook(Notebook notebook) throws SystemException {
        createDirectory();
        for (int i = 0; i < notebook.getSize();i++) {
            try {
                saveNoteContent(notebook.getNote(i));
                saveNoteDetails(notebook.getNote(i));
            } catch (IOException e) {
                throw new SystemException(SystemException.ExceptionType.EXCEPTION_FILE_CREATION_ERROR);
            }
        }
    }


    /**
     * Saves all the Events in the Timetable to the storage file.
     *
     * @param timetable The Timetable containing all the events to be saved.
     */
    private void saveTimetable(Timetable timetable){

    }

    /**
     * Saves all the Notes in the Notebook and the Events in the Timetable to the storage file.
     *
     * @param notebook The Notebook containing all the notes to be saved.
     * @param timetable The Timetable containing all the events to be saved.
     */
    public void saveAll(Notebook notebook, Timetable timetable)throws SystemException {
        saveNotebook(notebook);
        saveTimetable(timetable);
    }

    /**
     * Loads the Notebook and Timetable from the storage file.
     *
     * @param notebook The Notebook to be loaded into.
     * @param timeTable The Timetable to be loaded into.
     */
    public void loadAll(Notebook notebook, Timetable timeTable){

    }

    /**
     * Saves an individual note to the storage file.
     *
     * @param note The note to be saved
     */
    public static void saveNoteContent(Note note) throws IOException {
        String path = FOLDER_DIR + NOTES_DIR + "/" + note.getTitle() + ".txt";
        createFile(path);
        FileWriter fwAppend = new FileWriter(path, true);
        fwAppend.write(note.getContent().toString()); // Needs to be edited. String changed to Arraylist
        fwAppend.close();
    }

    /**
     * Saves the details of notes such as title, tags and pinned status to the notebook text file.
     * @param note Note of which details are to be saved to the file
     */
    private static void saveNoteDetails(Note note) throws IOException {
        String path = FOLDER_DIR + "/" + NOTEBOOK_FILE_PATH;
        createFile(path);
        FileWriter fwAppend = new FileWriter(path, true);
        String noteDetails = note.getTitle()
                            + DELIMITER
                            + note.getPinned()
                            + DELIMITER
                            + note.getTags()
                            + System.lineSeparator();
        fwAppend.write(noteDetails);
        fwAppend.close();
    }

    /**
     * Creates a directory path data/notes. In case both data and /notes do not exist.
     */
    private static void createDirectory() {
        String dataPath = FOLDER_DIR;
        String notePath = FOLDER_DIR + NOTES_DIR;
        File dataDirectory = new File(dataPath);
        File noteDirectory = new File(notePath);
        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
        }
        if (!noteDirectory.exists()) {
            noteDirectory.mkdir();
        }
    }

    /**
     * Checks if a file exists. If it does not, creates file with the input path
     * @param path path of file to be created
     * @throws IOException thrown when directory does not exist. Unable to create file
     */
    private static void createFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
}
