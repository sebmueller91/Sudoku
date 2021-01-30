package dgs_software.sudoku.utils;

import android.os.Build;
import android.util.Pair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dgs_software.sudoku.config.Constants;
import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.model.Sudoku;

public class Utils {
    // region Methods
    // Checks if an element with the same position exists in the list
    public static boolean listContainsElement(LinkedList<Pair<Integer, Integer>> list, Pair<Integer, Integer> element) {
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
    public static Pair<Integer, Integer> getPositionOfCell(Cell cell, Sudoku sudoku) {
        if (cell == null || sudoku == null || sudoku.getField() == null) {
            return null;
        }
        for (int i = 0; i < sudoku.getField().length; i++) {
            for (int j = 0; j < sudoku.getField()[i].length; j++) {
                if (sudoku.getField()[i][j] == cell) {
                    return new Pair(i,j);
                }
            }
        }
        return null;
    }

    // Convertes a 2d int array to a 2d Cell array and sets the values of the cells accordingly
    // The valuesIsFixed attribute is given as second argument
    public static Cell[][] intToCellArray(int[][] intArray, boolean valuesFixed) {
        Cell[][] cellArray = new Cell[intArray.length][intArray[0].length];
        for (int i = 0; i < cellArray.length; i++) {
            for (int j = 0; j < cellArray[i].length; j++) {
                cellArray[i][j] = new Cell(intArray[i][j]);
                if (cellArray[i][j].getValue() != 0) {
                    cellArray[i][j].setIsFixedValue(valuesFixed);
                }
            }
        }
        return cellArray;
    }

    // Creates an array of the given length with the indices from 0:length-1 in random order
    public static Integer[] createRandomOrderIndices(int length) {
        Integer[] indices = new Integer[length];
        for (int i = 0; i < length; i++) {
            indices[i] = i;
        }
        List list = Arrays.asList(indices);
        Collections.shuffle(list);
        list.toArray(indices);
        return indices;
    }

    // Converts the string to a boolean and returns the given defaultValue if no conversion is possible
    public static boolean tryParseBoolean(String value, boolean defaultValue) {
        if (value.toUpperCase().equals("TRUE")) {
            return true;
        } else if (value.toUpperCase().equals("FALSE")) {
            return false;
        } else {
            return defaultValue;
        }
    }
    // endregion Methods
}
