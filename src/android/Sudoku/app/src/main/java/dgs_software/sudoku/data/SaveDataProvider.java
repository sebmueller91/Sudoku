package dgs_software.sudoku.data;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import dgs_software.sudoku.config.GlobalConfig;
import dgs_software.sudoku.config.LanguageConfig;
import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.model.Sudoku;
import dgs_software.sudoku.utils.Utils;

// Provides methods to save and load app data to files in the internal storage
public class SaveDataProvider {
    // region Filenames
    private static final String SUDOKUPLAY_FILENAME_SHOWFAULTYCELLS = "save_sudokuplay_showfaultycells.save";
    private static final String SUDOKUPLAY_FILENAME_HIGHLIGHTCELLS = "save_sudokuplay_highlightcells.save";
    private static final String SUDOKUPLAY_FILENAME_SUDOKU = "save_sudokuplay_sudoku.save";

    private static final String SUDOKUSOLVER_FILENAME_SUDOKU = "save_sudokusolver_sudoku.save";

    private static final String LANGUAGEOVERRIDE_FILENAME = "save_languageoverride.save";
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
            Log.e(GlobalConfig.LOGTAG, "SaveDataProvider: context is zero");
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

    public boolean deleteSudokuPlay_sudoku() {
        return deleteFile(SUDOKUPLAY_FILENAME_SUDOKU);
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

    // region common methods
    public boolean save_languageOverride(LanguageConfig.SUPPORTED_LANGUAGES  language) {
        String languageString = language.toString();
        return saveStringToFile(languageString, LANGUAGEOVERRIDE_FILENAME);
    }

    public LanguageConfig.SUPPORTED_LANGUAGES load_languageOverride() {
        String fileContent = loadStringFromFile(LANGUAGEOVERRIDE_FILENAME);
        if (fileContent == null) {
            return null;
        }

        return LanguageConfig.stringToSupportedLanguage(fileContent);
    }

    public boolean delete_LanguageOverride() {
        return deleteFile(LANGUAGEOVERRIDE_FILENAME);
    }
    // endregion common methods

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
            Log.e(GlobalConfig.LOGTAG, "SaveDataProvider saveStringToFile: file <" + filename + "> does not exist");
            return false;
        }

        try {
            fileOutputStream.write(value.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e(GlobalConfig.LOGTAG, "SaveDataProvider saveStringToFile: error when writing to file", e);
            return false;
        }

        return true;
    }

    // Loads a boolean from the file "filename" in the internal storage
    // In case of error, the default value is returned
    private boolean loadBooleanFromFile(boolean defaultValue, String filename) {
        String fileContent = loadStringFromFile(filename);
        if (fileContent == null) {
            Log.e(GlobalConfig.LOGTAG, "SaveDataProvider loadBooleanFromFile: file <" + filename + "> does not exist");
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
            Log.e(GlobalConfig.LOGTAG, "SaveDataProvider loadStringFromFile: File " + filename + " could not be found");
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
            Log.e(GlobalConfig.LOGTAG, "SaveDataProvider loadStringFromFile: Error when reading from file " + filename + " ", e);
            return null;
        }

        return fileContent;
    }

    private boolean deleteFile(String filename) {
        Log.d(GlobalConfig.LOGTAG, "Deleting file " + filename);
        File file = new File(getContext().getFilesDir(), filename);
        return file.exists() && file.isFile() && file.delete();
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
        // Write Cell Values
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sudoku.getField().length; i++) {
            for (int j = 0; j < sudoku.getField()[i].length; j++) {
                stringBuilder.append(sudoku.getField()[i][j].getValue());
                stringBuilder.append(GlobalConfig.NUMBER_DELIMITER);
            }
            stringBuilder.append(GlobalConfig.ROW_DELIMITER);
        }

        // Write isFixedValue
        stringBuilder.append(GlobalConfig.SUDOKU_DELIMITER);
        for (int i = 0; i < sudoku.getField().length; i++) {
            for (int j = 0; j < sudoku.getField()[i].length; j++) {
                stringBuilder.append(sudoku.getField()[i][j].getIsFixedValue());
                stringBuilder.append(GlobalConfig.NUMBER_DELIMITER);
            }
            stringBuilder.append(GlobalConfig.ROW_DELIMITER);
        }

        // Write NoteValues (if necessary)
        stringBuilder.append(GlobalConfig.SUDOKU_DELIMITER);
        if (includeNotes) {
            for (int i = 0; i < sudoku.getField().length; i++) {
                for (int j = 0; j < sudoku.getField()[i].length; j++) {
                    for (int k = 0; k < 9; k++) {
                        if (sudoku.getField()[i][j].getActiveNotes() != null) {
                            stringBuilder.append(sudoku.getField()[i][j].getActiveNotes()[k]);
                        } else {
                            stringBuilder.append(false);
                        }
                        stringBuilder.append(GlobalConfig.NOTES_DELIMITER);
                    }
                    stringBuilder.append(GlobalConfig.NUMBER_DELIMITER);
                }
                stringBuilder.append(GlobalConfig.ROW_DELIMITER);
            }
        }

        // Write Difficulty
        stringBuilder.append(GlobalConfig.SUDOKU_DELIMITER);
        stringBuilder.append(sudoku.getDifficulty().toString());

        // Write ElapsedSeconds
        stringBuilder.append(GlobalConfig.SUDOKU_DELIMITER);
        stringBuilder.append(sudoku.getElapsedSeconds());

        return stringBuilder.toString();
    }

    // Converts a string into a sudoku
    // The data format is the same as in the inverse method above
    private static Sudoku stringToSudoku(String string) {
        // parts[0] contains the sudoku
        // parts[1] contains information about each cell if the value is fixed
        // parts[3] (if != null) contains the 9 notes for each cell
        try {
            String[] parts = string.split(GlobalConfig.SUDOKU_DELIMITER);

            Cell[][] field = new Cell[9][9];

            // Read the values for all cells from the string
            if (parts != null && parts.length >= 1) {
                String[] rows = parts[0].split(GlobalConfig.ROW_DELIMITER);
                for (int i = 0; i < field.length; i++) {
                    String[] entries = rows[i].split(GlobalConfig.NUMBER_DELIMITER);
                    for (int j = 0; j < field[i].length; j++) {
                        field[i][j] = new Cell(Integer.parseInt(entries[j]));
                    }
                }
            }

            // Read the isFixedValue for each cell from the String
            if (parts != null && parts.length >= 2) {
                String[] rows = parts[1].split(GlobalConfig.ROW_DELIMITER);
                for (int i = 0; i < field.length; i++) {
                    String[] entries = rows[i].split(GlobalConfig.NUMBER_DELIMITER);
                    for (int j = 0; j < field[i].length; j++) {
                        field[i][j].setIsFixedValue(Boolean.parseBoolean(entries[j]));
                    }
                }
            }

            // Read the Note values for all cells (if existent
            if (parts != null && parts.length >= 3 && parts[2].isEmpty() == false) {
                String[] rows = parts[2].split(GlobalConfig.ROW_DELIMITER);
                for (int i = 0; i < field.length; i++) {
                    String[] cells = rows[i].split(GlobalConfig.NUMBER_DELIMITER);
                    for (int j = 0; j < field[i].length; j++) {
                        String[] notes = cells[j].split(GlobalConfig.NOTES_DELIMITER);
                        field[i][j].setActiveNotes(new boolean[9]);
                        for (int k = 0; k < notes.length; k++) {
                            field[i][j].getActiveNotes()[k] = Boolean.parseBoolean(notes[k]);
                        }
                    }
                }
            }

            // Read the difficulty for the sudoku
            Sudoku.Difficulty difficulty = Sudoku.Difficulty.RELOAD_EXISTING;
            if (parts != null && parts.length >= 4) {

                if (parts[3].toUpperCase().equals(Sudoku.Difficulty.EASY.toString().toUpperCase())) {
                    difficulty = Sudoku.Difficulty.EASY;
                } else if (parts[3].toUpperCase().equals(Sudoku.Difficulty.MEDIUM.toString().toUpperCase())) {
                    difficulty = Sudoku.Difficulty.MEDIUM;
                }
                if (parts[3].toUpperCase().equals(Sudoku.Difficulty.HARD.toString().toUpperCase())) {
                    difficulty = Sudoku.Difficulty.HARD;
                }
            }

            // Read Elapsed Seconds of the sudoku
            int elapsedSeconds = 0;
            if (parts != null && parts.length >= 5) {
                elapsedSeconds = Integer.parseInt(parts[4]);
            }

            return new Sudoku(field, difficulty, elapsedSeconds);

        } catch (NumberFormatException e) {
            Log.e(GlobalConfig.LOGTAG, "SaveDataProvider stringToSudoku: Error when parsing save data", e);
            return null;
        }
    }
    // endregion Helper Methods
    // endregion Methods
}
