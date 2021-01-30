package dgs_software.sudoku.data;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import dgs_software.sudoku.config.Constants;
import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.model.Sudoku;
import dgs_software.sudoku.utils.Utils;

import static android.content.Context.MODE_PRIVATE;

// Provides methods to save and load app data to files in the internal storage
public class SaveDataProvider {
    // region Filenames
    private static final String SUDOKUPLAY_FILENAME_SHOWFAULTYCELLS = "save_sudokuplay_showfaultycells.save";
    private static final String SUDOKUPLAY_FILENAME_HIGHLIGHTCELLS = "save_sudokuplay_highlightcells.save";
    private static final String SUDOKUPLAY_FILENAME_SUDOKU = "save_sudokuplay_sudoku.save";

    private static final String SUDOKUSOLVER_FILENAME_SUDOKU = "save_sudokusolver_sudoku.save";
    // endregion Filenames

    // region Attributes
    private Context m_context;

    public Context getContext() {
        return m_context;
    }

    public void setContext(Context context) {
        this.m_context = context;
    }
    // endregion Attributes

    // region Constructor
    public SaveDataProvider(Context context) {
        if (context == null) {
            // TODO: Write Log
        }
        this.m_context = context;
    }
    // endregion Constructor

    // region Methods

    // region SudokuPlay Methods
    public boolean saveSudokuPlayPreferences_showFaultyCells(boolean showFaultyCells) {
        return saveBooleanToFile(showFaultyCells, SUDOKUPLAY_FILENAME_SHOWFAULTYCELLS);
    }

    public boolean loadSudokuPlayPreferences_showFaultyCells(boolean defaultValue) {
        return loadBooleanFromFile(defaultValue, SUDOKUPLAY_FILENAME_SHOWFAULTYCELLS);
    }

    public boolean saveSudokuPlayPreferences_highlightCells(boolean highlightCells) {
        return saveBooleanToFile(highlightCells, SUDOKUPLAY_FILENAME_HIGHLIGHTCELLS);
    }

    public boolean loadSudokuPlayPreferences_highlightCells(boolean defaultValue) {
        return loadBooleanFromFile(defaultValue, SUDOKUPLAY_FILENAME_HIGHLIGHTCELLS);
    }

    public boolean saveSudokuPlay_sudoku(Sudoku sudoku) {
        String sudokuString = sudokuToString(sudoku, true);
        return saveStringToFile(sudokuString, SUDOKUPLAY_FILENAME_SUDOKU);
    }

    public Sudoku loadSudokuPlay_sudoku() {
        String fileContent = loadStringFromFile(SUDOKUPLAY_FILENAME_SUDOKU);
        if (fileContent == null) {
            return null;
        }
        return stringToSudoku(fileContent);
    }
    // endregion SudokuPlay Methods

    // region SudokuSolver Methods
    public boolean saveSudokuSolver_sudoku(Sudoku sudoku) {
        String sudokuString = sudokuToString(sudoku, false);
        return saveStringToFile(sudokuString, SUDOKUSOLVER_FILENAME_SUDOKU);
    }

    public Sudoku loadSudokuSolver_sudoku() {
        String fileContent = loadStringFromFile(SUDOKUSOLVER_FILENAME_SUDOKU);
        if (fileContent == null) {
            return null;
        }
        return stringToSudoku(fileContent);
    }
    // endregion SudokuSolver Methods

    // region IOMethods
    // Saves a boolean to a file "filename" in the internal storage
    // Returns true on success, false otherwise
    private boolean saveBooleanToFile(boolean value, String filename) {
        saveStringToFile(Boolean.toString(value), filename);

        return true;
    }

    // Saves a string to a file "filename" in the internal storage
    // Returns true on success, false otherwise
    private boolean saveStringToFile(String value, String filename) {
        File filesDir = getContext().getFilesDir();

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            // TODO: Log
            return false;
        }

        try {
            fileOutputStream.write(value.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            // TODO: Log
            return false;
        }

        return true;
    }

    // Loads a boolean from the file "filename" in the internal storage
    // In case of error, the default value is returned
    private boolean loadBooleanFromFile(boolean defaultValue, String filename) {
        String fileContent = loadStringFromFile(filename);
        if (fileContent == null) {
            // TODO: Log
            return defaultValue;
        }

        return Utils.tryParseBoolean(fileContent, defaultValue);
    }

    // Loads a string from the file "filename" in the internal storage
    // In case of error, the null is returned
    private String loadStringFromFile(String filename) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = getContext().openFileInput(filename);
        } catch (FileNotFoundException e) {
            // TODO: Log
            return null;
        }

        int c;
        String fileContent = "";
        try {
            while ((c = fileInputStream.read()) != -1) {
                fileContent = fileContent + Character.toString((char) c);
            }

            fileInputStream.close();
        } catch (IOException e) {
            // TODO: Log
            return null;
        }

        return fileContent;
    }
    // endregion IOMethods

    // region Helper Methods

    // Converts a sudoku into a string
    // The data format is as follows:
    // (1) Values of all cells
    // (2) isFixed value of each cell
    // (3) note Values for each cell
    // (4) difficulty of the sudoku
    // The different values are separated by different sparators
    public static String sudokuToString(Sudoku sudoku, boolean includeNotes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sudoku.getField().length; i++) {
            for (int j = 0; j < sudoku.getField()[i].length; j++) {
                stringBuilder.append(sudoku.getField()[i][j].getValue());
                stringBuilder.append(Constants.NUMBER_DELIMITER);
            }
            stringBuilder.append(Constants.ROW_DELIMITER);
        }

        stringBuilder.append(Constants.SUDOKU_DELIMITER);
        for (int i = 0; i < sudoku.getField().length; i++) {
            for (int j = 0; j < sudoku.getField()[i].length; j++) {
                stringBuilder.append(sudoku.getField()[i][j].getIsFixedValue());
                stringBuilder.append(Constants.NUMBER_DELIMITER);
            }
            stringBuilder.append(Constants.ROW_DELIMITER);
        }

        stringBuilder.append(Constants.SUDOKU_DELIMITER);
        if (includeNotes) {
            for (int i = 0; i < sudoku.getField().length; i++) {
                for (int j = 0; j < sudoku.getField()[i].length; j++) {
                    for (int k = 0; k < 9; k++) {
                        if (sudoku.getField()[i][j].getActiveNotes() != null) {
                            stringBuilder.append(sudoku.getField()[i][j].getActiveNotes()[k]);
                        } else {
                            stringBuilder.append(false);
                        }
                        stringBuilder.append(Constants.NOTES_DELIMITER);
                    }
                    stringBuilder.append(Constants.NUMBER_DELIMITER);
                }
                stringBuilder.append(Constants.ROW_DELIMITER);
            }
        }

        stringBuilder.append(Constants.SUDOKU_DELIMITER);
        stringBuilder.append(sudoku.getDifficulty().toString());

        return stringBuilder.toString();
    }

    // Converts a string into a sudoku
    // The data format is the same as in the inverse method above
    private static Sudoku stringToSudoku(String string) {
        // parts[0] contains the sudoku
        // parts[1] contains information about each cell if the value is fixed
        // parts[3] (if != null) contains the 9 notes for each cell
        String[] parts = string.split(Constants.SUDOKU_DELIMITER);

        Cell[][] field = new Cell[9][9];

        // Read the values for all cells from the string
        if (parts != null && parts.length >= 1) {
            String[] rows = parts[0].split(Constants.ROW_DELIMITER);
            for (int i = 0; i < field.length; i++) {
                String[] entries = rows[i].split(Constants.NUMBER_DELIMITER);
                for (int j = 0; j < field[i].length; j++) {
                    field[i][j] = new Cell(Integer.parseInt(entries[j]));
                }
            }
        }

        // Read the isFixedValue for each cell from the String
        if (parts != null && parts.length >= 2) {
            String[] rows = parts[1].split(Constants.ROW_DELIMITER);
            for (int i = 0; i < field.length; i++) {
                String[] entries = rows[i].split(Constants.NUMBER_DELIMITER);
                for (int j = 0; j < field[i].length; j++) {
                    field[i][j].setIsFixedValue(Boolean.parseBoolean(entries[j]));
                }
            }
        }

        // Read the Note values for all cells (if existent
        if (parts != null && parts.length >= 3 && parts[2].isEmpty() == false) {
            String[] rows = parts[2].split(Constants.ROW_DELIMITER);
            for (int i = 0; i < field.length; i++) {
                String[] cells = rows[i].split(Constants.NUMBER_DELIMITER);
                for (int j = 0; j < field[i].length; j++) {
                    String[] notes = cells[j].split(Constants.NOTES_DELIMITER);
                    field[i][j].setActiveNotes(new boolean[9]);
                    for (int k = 0; k < notes.length; k++) {
                        field[i][j].getActiveNotes()[k] = Boolean.parseBoolean(notes[k]);
                    }
                }
            }
        }

        // Read the difficulty for the sudoku
        Sudoku.Difficulty difficulty = Sudoku.Difficulty.NONE;
        if (parts != null && parts.length >= 4) {

            if (parts[3].toUpperCase().equals(Sudoku.Difficulty.EASY.toString().toUpperCase())) {
                difficulty = Sudoku.Difficulty.EASY;
            } else if (parts[3].toUpperCase().equals(Sudoku.Difficulty.MEDIUM.toString().toUpperCase())) {
                difficulty = Sudoku.Difficulty.MEDIUM;
            } if (parts[3].toUpperCase().equals(Sudoku.Difficulty.HARD.toString().toUpperCase())) {
                difficulty = Sudoku.Difficulty.HARD;
            }

        }

        return new Sudoku(field, difficulty);
    }
    // endregion Helper Methods
    // endregion Methods
}