package dgs_software.sudoku.data;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

import dgs_software.sudoku.R;
import dgs_software.sudoku.config.GlobalConfig;
import dgs_software.sudoku.model.Sudoku;

public class RessourcesDataProvider {

    // region Attributes
    private Context m_context;

    public Context getContext() {
        return this.m_context;
    }

    public void setContext(Context context) {
        this.m_context = context;
    }
    // endregion

    // region Constructor
    public RessourcesDataProvider(Context context) {
        setContext(context);
    }
    // endregion

    // region Methods
    // Reads one of the three existing files and returns the sudokuList extracted from it
    public LinkedList<int[][]> getSudokuListFromFile(Sudoku.Difficulty difficulty) {
        InputStream fileStream = null;
        switch (difficulty) {
            case EASY:
                fileStream = getContext().getResources().openRawResource(R.raw.sudokus_easy);
                break;
            case MEDIUM:
                fileStream = getContext().getResources().openRawResource(R.raw.sudokus_normal);
                break;
            case HARD:
                fileStream = getContext().getResources().openRawResource(R.raw.sudokus_hard);
                break;
        }
        String fileContent = "";
        try {
            fileContent = inputStreamToString(fileStream);
        } catch (IOException e) {
            System.err.println("IOException when reading sudoku file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return stringToSudokuList(fileContent);
    }

    // Converts the given input stream (e.g. a file) to a String
    private static String inputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
        }

        return stringBuilder.toString();
    }

    // Expects a string that contains multiple sudokus that are seperated by the specified delimiters
    // Returns a list of 2d int array sudokus
    private static LinkedList<int[][]> stringToSudokuList(String fileContent) {
        LinkedList<int[][]> sudokuList = new LinkedList<int[][]>();
        String[] sudokus = fileContent.split(GlobalConfig.SUDOKU_DELIMITER);
        for (int s = 0; s < sudokus.length; s++) {
            int[][] sudoku = stringToSudoku(sudokus[s]);
            sudokuList.add(sudoku);
        }

        return sudokuList;
    }

    // Converts a string into a sudoku
    private static int[][] stringToSudoku(String string) {
        int[][] sudoku = new int[9][9];
        String[] rows = string.split(GlobalConfig.ROW_DELIMITER);
        for (int i = 0; i < rows.length; i++) {
            String[] entries = rows[i].split(GlobalConfig.NUMBER_DELIMITER);
            for (int j = 0; j < entries.length; j++) {
                sudoku[i][j] = Integer.parseInt(entries[j]);
            }
        }
        return sudoku;
    }

    // endregion Methods
}
