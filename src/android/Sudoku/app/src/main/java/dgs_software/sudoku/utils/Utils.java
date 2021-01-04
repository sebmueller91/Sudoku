package dgs_software.sudoku.utils;

import android.os.Build;
import android.util.Pair;
import androidx.annotation.RequiresApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.model.Sudoku;

public class Utils {
    private static final String SUDOKU_DELIMITER = ";", ROW_DELIMITER = "-", NUMBER_DELIMITER = ",";

    // Checks if an element with the same position exists in the list
    public static boolean ListContainsElement(LinkedList<Pair<Integer, Integer>> list, Pair<Integer, Integer> element) {
        if (list == null || element == null) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(element)) {
                return true;
            }
        }
        return false;
    }

    // Searches the sudoku field for the given cell and returns its row and column as a pair
    public static Pair<Integer, Integer> GetPositionOfCell(Cell cell, Sudoku sudoku) {
        if (cell == null || sudoku == null || sudoku.GetField() == null) {
            return null;
        }
        for (int i = 0; i < sudoku.GetField().length; i++) {
            for (int j = 0; j < sudoku.GetField()[i].length; j++) {
                if (sudoku.GetField()[i][j] == cell) {
                    return new Pair(i,j);
                }
            }
        }
        return null;
    }

    // Converts the given input stream (e.g. a file) to a String
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String InputStreamToString(InputStream inputStream) throws IOException {
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
    public static LinkedList<int[][]> FileContentToSudokuList(String fileContent) {
        LinkedList<int[][]> sudokuList = new LinkedList<int[][]>();
        String[] sudokus = fileContent.split(SUDOKU_DELIMITER);
        for (int s = 0; s < sudokus.length; s++) {
            int[][] sudoku = new int[9][9];
            String[] rows = sudokus[s].split(ROW_DELIMITER);
            for (int i = 0; i < rows.length; i++) {
                String[] entries = rows[i].split(NUMBER_DELIMITER);
                for (int j = 0; j < entries.length; j++) {
                    sudoku[i][j] = Integer.parseInt(entries[j]);
                }
            }
            sudokuList.add(sudoku);
        }

        return sudokuList;
    }

    // Convertes a 2d int array to a 2d Cell array and sets the values of the cells accordingly
    // The valuesIsFixed attribute is given as second argument
    public static Cell[][] IntToCellArray(int[][] intArray, boolean valuesFixed) {
        Cell[][] cellArray = new Cell[intArray.length][intArray[0].length];
        for (int i = 0; i < cellArray.length; i++) {
            for (int j = 0; j < cellArray[i].length; j++) {
                cellArray[i][j] = new Cell(intArray[i][j]);
                cellArray[i][j].SetIsFixedValue(valuesFixed);
            }
        }
        return cellArray;
    }
}
