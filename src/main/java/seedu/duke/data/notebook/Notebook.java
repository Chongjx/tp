package seedu.duke.data.notebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a Notebook object. Contains all the notes.
 */
public class Notebook {

    private ArrayList<Note> notes;
    private ArrayList<Note> archivedNotes;

    /**
     * Creates a new list of notes.
     */
    public Notebook() {
        notes = new ArrayList<>();
        archivedNotes = new ArrayList<>();
    }

    /**
     * Creates a list of notes from existing data.
     *
     * @param notes of existing data.
     */
    public Notebook(ArrayList<Note> notes) {
        this.notes = notes;
    }

    /**
     * Gets a list of notes from existing data.
     *
     * @return notes of existing data.
     */
    public ArrayList<Note> getNotes() {
        return this.notes;
    }

    public boolean checkPinned() {
        for (Note note : notes) {
            if (note.getPinned()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Note> getPinnedNotes() {
        ArrayList<Note> noteList = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getPinned()) {
                noteList.add(notes.get(i));
            }
        }
        return noteList;
    }

    public ArrayList<Note> getPinnedNotes(ArrayList<Note> noteArrayList) {
        ArrayList<Note> noteList = new ArrayList<>();
        for (int i = 0; i < noteArrayList.size(); i++) {
            if (noteArrayList.get(i).getPinned()) {
                noteList.add(noteArrayList.get(i));
            }
        }
        return noteList;
    }

    public ArrayList<Note> getUnpinnedNotes() {
        ArrayList<Note> noteList = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            if (!notes.get(i).getPinned()) {
                noteList.add(notes.get(i));
            }
        }
        return noteList;
    }

    public ArrayList<Note> getUnpinnedNotes(ArrayList<Note> noteArrayList) {
        ArrayList<Note> noteList = new ArrayList<>();
        for (int i = 0; i < noteArrayList.size(); i++) {
            if (!noteArrayList.get(i).getPinned()) {
                noteList.add(noteArrayList.get(i));
            }
        }
        return noteList;
    }

    /**
     * Method compiles the ArrayList items and appends the items to a String.
     * The ArrayList has already been sorted
     * Method returns either top to bottom or bottom to top to account for ascending/descending sorting
     *
     * @return noteString String containing the notes sorted either ascending ot descending
     */
    public ArrayList<Note> getSortedList(Boolean isAscendingOrder, Boolean isPinned) {
        ArrayList<Note> sortedNotes = new ArrayList<>();

        if (isPinned == null) {
            // Takes the notes in the notebook and sorts them according to title, alphabetically (a-z)
            sortedNotes = (ArrayList<Note>) notes.stream()
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(a -> a.getTitle().toLowerCase()))
                    .collect(Collectors.toList());
        } else if (!isPinned) {
            sortedNotes = (ArrayList<Note>) notes.stream()
                    .filter(Objects::nonNull)
                    .filter((s) -> !s.getPinned())
                    .sorted(Comparator.comparing(a -> a.getTitle().toLowerCase()))
                    .collect(Collectors.toList());
        } else if (isPinned) {
            sortedNotes = (ArrayList<Note>) notes.stream()
                    .filter(Objects::nonNull)
                    .filter((s) -> s.getPinned())
                    .sorted(Comparator.comparing(a -> a.getTitle().toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (!isAscendingOrder) {
            Collections.reverse(sortedNotes);
        }

        return sortedNotes;
    }

    /**
     * Method compiles the ArrayList items and appends the items to a String.
     * The ArrayList has already been sorted
     * Method returns either top to bottom or bottom to top to account for ascending/descending sorting
     *
     * @return noteString String containing the notes sorted either ascending ot descending
     */
    public ArrayList<Note> getSortedList(Boolean isAscendingOrder, Boolean isPinned, ArrayList<Note> noteArrayList) {
        ArrayList<Note> sortedNotes = new ArrayList<>();

        if (isPinned) {
            sortedNotes = (ArrayList<Note>) noteArrayList.stream()
                    .filter(Objects::nonNull)
                    .filter((s) -> s.getPinned())
                    .sorted(Comparator.comparing(a -> a.getTitle().toLowerCase()))
                    .collect(Collectors.toList());
        } else if (!isPinned) {
            sortedNotes = (ArrayList<Note>) noteArrayList.stream()
                    .filter(Objects::nonNull)
                    .filter((s) -> !s.getPinned())
                    .sorted(Comparator.comparing(a -> a.getTitle().toLowerCase()))
                    .collect(Collectors.toList());
        } else if (isPinned == null) {
            sortedNotes = (ArrayList<Note>) noteArrayList.stream()
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(a -> a.getTitle().toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (!isAscendingOrder) {
            Collections.reverse(sortedNotes);
        }

        return sortedNotes;
    }



    public Note getNote(int index) {
        return notes.get(index);
    }

    public Note getNote(String noteTitle, boolean isArchive) {
        if (isArchive) {
            return notes.stream()
                    .filter((s) -> s.getTitle().equalsIgnoreCase(noteTitle))
                    .findFirst().get();
        }

        return archivedNotes.stream()
                .filter((s) -> s.getTitle().equalsIgnoreCase(noteTitle))
                .findFirst().get();
    }

    public boolean getNote(String noteTitle) {
        return notes.stream().anyMatch(note -> note.getTitle().equalsIgnoreCase(noteTitle));
    }

    /**
     * Adds a note into the notebook.
     */
    public void addNote(Note note) {
        notes.add(note);
    }

    /**
     * Removes a note from the notebook.
     */
    public boolean deleteNote(int index) {
        notes.remove(index);
        return true;
    }

    /**
     * Removes a note from the notebook.
     */
    public boolean deleteNote(String title) {
        return notes.removeIf(note -> note.getTitle().equalsIgnoreCase(title));
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public String archiveNotes(int index) {
        Note archivedNote = notes.get(index);

        archivedNotes.add(archivedNote);
        notes.remove(index);

        return archivedNote.getTitle();
    }

    public boolean archiveNotes(String noteTitle) {
        boolean isDeleted;

        Note archivedNote = getNote(noteTitle, true);
        isDeleted = deleteNote(noteTitle);

        if (isDeleted) {
            archivedNotes.add(archivedNote);
        }

        return isDeleted;
    }

    public String unarchiveNotes(int index) {
        Note unarchivedNote = archivedNotes.get(index);

        notes.add(unarchivedNote);
        archivedNotes.remove(unarchivedNote);

        return unarchivedNote.getTitle();
    }

    public boolean unarchiveNotes(String noteTitle) {
        boolean isDeleted;

        Note unarchivedNote = getNote(noteTitle, false);
        isDeleted = archivedNotes.remove(unarchivedNote);

        if (isDeleted) {
            notes.add(unarchivedNote);
        }

        return isDeleted;
    }

    public ArrayList<Note> getArchivedNotes() {
        return archivedNotes;
    }

    public int getSize() {
        return notes.size();
    }
}
