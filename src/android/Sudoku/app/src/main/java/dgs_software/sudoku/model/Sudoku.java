package dgs_software.sudoku.model;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import java.util.LinkedList;

import dgs_software.sudoku.config.GlobalConfig;
import dgs_software.sudoku.data.RessourcesDataProvider;
import dgs_software.sudoku.utils.Logger;
import dgs_software.sudoku.utils.Utils;

public class Sudoku {
    // region Enum Difficulty declaration
    public enum Difficulty {
        EASY(1), MEDIUM(2), HARD(3), RELOAD_EXISTING(4); // Difficulty RELOAD_EXISTING is used for user created sudokus (in SudokuSolver)

        private int m_difficulty;

        Difficulty(int difficulty) {
            this.m_difficulty = difficulty;
        }

        public int getIntVal() {
            return m_difficulty;
        }


        public static Difficulty intValToDifficulty(int value) {
            if (value == Difficulty.EASY.getIntVal()) {
                return Difficulty.EASY;
            } else if (value == Difficulty.MEDIUM.getIntVal()) {
                return Difficulty.MEDIUM;
            } else if (value == Difficulty.HARD.getIntVal()) {
                return Difficulty.HARD;
            } else if (value == Difficulty.RELOAD_EXISTING.getIntVal()) {
                return Difficulty.RELOAD_EXISTING;
            } else {
                return null;
            }
        }
    }
    // endregion ENUM Difficulty declaration

    // region Attributes
    // region Field
    private Cell[][] m_field = null;

    public Cell[][] getField() {
        return this.m_field;
    }

    public void setField(Cell[][] field) {
        this.m_field = field;
    }
    // endregion Field

    // region Difficulty
    private Difficulty m_difficulty;

    public Difficulty getDifficulty() {
        return m_difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.m_difficulty = difficulty;
    }

    // endregion Difficulty

    // region ElapsedSeconds
    private int m_elapsedSeconds = 0;

    public int getElapsedSeconds() {
        return  m_elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        m_elapsedSeconds = elapsedSeconds;
    }
    // endregion ElapsedSeconds
    // endregion Attributes

    // region Constructors
    public Sudoku(Cell[][] field) {
        this(field, Difficulty.RELOAD_EXISTING);
    }

    public Sudoku(Cell[][] field, Difficulty difficulty) {
        setDifficulty(difficulty);
        setField(field);
    }

    public Sudoku(Cell[][] field, Difficulty difficulty, int elapsedSeconds) {
        setDifficulty(difficulty);
        setField(field);
        setElapsedSeconds(elapsedSeconds);
    }

    // Loads a sudoku with given difficulty from the raw value files
    public Sudoku(Difficulty difficulty, Context context) {
        if (difficulty == Difficulty.RELOAD_EXISTING) {
            Logger.LogError("Sudoku consutuctor: Difficulty is invalid!");
            return;
        }

        setDifficulty(difficulty);

        // Retrieve list of sudokus from data file
        RessourcesDataProvider dataProvider = new RessourcesDataProvider(context);
        LinkedList<int[][]> sudokuList =  dataProvider.getSudokuListFromFile(difficulty);

        // Choose a random sudoku from the list and set it as sudoku attribute
        double numberSudokus = (double) sudokuList.size();
        int randomIndex = (int) (Math.random() * numberSudokus);
        int[][] sudoku = sudokuList.get(randomIndex);
        setField(Utils.intToCellArray(sudoku, true));

        // TODO: Remove
        // Use this for testing to no have to solve sudoku each time
        getSolution();
        getField()[0][0].setValue(0);
        getField()[0][0].setIsFixedValue(false);
    }

    // endregion Constructors

    // region Methods

    // Returns true if a solution for the given Field was found, false if no solution for the given Field exists
    // For the true case, the variable "field" will be filled with the found solution
    public boolean getSolution() {
        // Some error in sudoku -> Can not be solved
        if (!isValid()) {
            return false;
        }

        // No errors + completely filled -> we have found a solution
        if (isCompletelyFilled()) {
            return true;
        }

        // Try every possibility
        for (int i = 0; i < getField().length; i++) {
            for (int j = 0; j < getField()[i].length; j++) {
                if (getField()[i][j].getValue() == 0) {
                    boolean[] possibleNumbers = getPossibleNumbers(i, j);
                    Integer[] randomOrderIndices = Utils.createRandomOrderIndices(possibleNumbers.length);
                    for (int k = 0; k < randomOrderIndices.length; k++) {
                        if (possibleNumbers[randomOrderIndices[k]]) {
                            getField()[i][j].setValue(randomOrderIndices[k] + 1);
                            if (getSolution()) {
                                // Solution found -> Reach it back up
                                return true;
                            }
                            getField()[i][j].setValue(0);
                        }
                    }
                    return false;
                }
            }
        }
        return false;
    }

    // Returns an arrary of boolean with length 9 that indicates which numbers could be added into this cell without breaking the Sudoku rules
    public boolean[] getPossibleNumbers(int row, int col) {
        // Array that contains pre-set of possible numbers
        boolean[] possibleNumbers = new boolean[9];
        for (int i = 0; i < 9; i++) {
            possibleNumbers[i] = true;
        }

        for (int i = 0; i < 9; i++) {
            // Remove numbers that are present in column
            if (i != col && getField()[row][i].getValue() != 0) {
                possibleNumbers[getField()[row][i].getValue() - 1] = false;
            }
            // Remove numbers that are present in row
            if (i != row && getField()[i][col].getValue() != 0) {
                possibleNumbers[getField()[i][col].getValue() - 1] = false;
            }
        }

        // Remove numbers that are present in block
        int blockStartRow = (row / 3) * 3; // Get top left indices of current block
        int blockStartCol = (col / 3) * 3;
        // Traverse block and remove present numbers
        for (int i = blockStartRow; i < blockStartRow + 3; i++) {
            for (int j = blockStartCol; j < blockStartCol + 3; j++) {
                if (i != row && j != col && getField()[i][j].getValue() != 0) {
                    possibleNumbers[getField()[i][j].getValue() - 1] = false;
                }
            }
        }

        return possibleNumbers;
    }

    // Returns true if the sudoku is completely filled without any error
    public boolean isSolved() {
        return isCompletelyFilled() && isValid();
    }

    // Returns true if all cells of the field are containing a number, false otherwise (if any cell is empty)
    public boolean isCompletelyFilled() {
        for (int i = 0; i < getField().length; i++) {
            for (int j = 0; j < getField()[i].length; j++) {
                if (getField()[i][j].getValue() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // Returns true if there are no collisions between numbers in the Sudoku, false if there is any error
    public boolean isValid() {
        // Check rows for duplicate entries
        for (int i = 0; i < 9; i++) {
            boolean[] numbers = new boolean[9];
            for (int j = 0; j < 9; j++) {
                if (getField()[i][j].getValue() != 0) { // Only check if cell is not empty
                    if (numbers[getField()[i][j].getValue() - 1] == false) { // Number has not been present in this row before
                        numbers[getField()[i][j].getValue() - 1] = true; // Mark number as present in this row
                    } else {
                        return false; // Number is present twice in this row
                    }
                }
            }
        }

        // Check columns for duplicate entries
        for (int i = 0; i < 9; i++) {
            boolean[] numbers = new boolean[9];
            for (int j = 0; j < 9; j++) {
                if (getField()[j][i].getValue() != 0) { // Only if cell is not empty
                    if (numbers[getField()[j][i].getValue() - 1] == false) { // Numbers has not been present in this column
                        numbers[getField()[j][i].getValue() - 1] = true; // Mark number as present in this column
                    } else {
                        return false; // Number is present twice in this column
                    }
                }
            }
        }

        // Check blocks for duplicate entries
        for (int i = 0; i < 9; i = i + 3) {
            for (int j = 0; j < 9; j = j + 3) {
                // i and j will be the indices of the left top of each block at this point

                // Traverse the individual 3x3 blocks
                boolean[] numbers = new boolean[9];
                for (int ii = i; ii < i + 3; ii++) {
                    for (int jj = j; jj < j + 3; jj++) {
                        if (getField()[ii][jj].getValue() != 0) { // Only if cell is not empty
                            if (numbers[getField()[ii][jj].getValue() - 1] == false) { // Number has not been present in this block
                                numbers[getField()[ii][jj].getValue() - 1] = true; // Mark number as present in this block
                            } else { // Number is present twice in this block
                                return false;
                            }
                        }

                    }
                }
            }
        }

        // No errors found
        return true;
    }

    // Returns a list of <row,column> pairs that are in conflict with any other cell in the sudoku
    public LinkedList<Pair<Integer, Integer>> getListOfWrongValues() {
        LinkedList<Pair<Integer, Integer>> wrongCells = new LinkedList<Pair<Integer, Integer>>();
        for (int i = 0; i < getField().length; i++) {
            for (int j = 0; j < getField()[i].length; j++) {
                if (getField()[i][j].getIsEmpty() == true) {
                    continue;
                }
                boolean[] possibleNumbers = getPossibleNumbers(i, j);
                if (possibleNumbers[getField()[i][j].getValue() - 1] == false) { // The entered number is not valid
                    wrongCells.add(new Pair<Integer, Integer>(i, j));
                }
            }
        }
        return wrongCells;
    }

    // Deletes all values which
    public void deleteNonFixedValues() {
        for (int i = 0; i < getField().length; i++) {
            for (int j = 0; j < getField()[i].length; j++) {
                if (getField()[i][j].getIsFixedValue() == false) {
                    getField()[i][j].setValue(0);
                }
            }
        }
    }

    // endregion Methods
}
