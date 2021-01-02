package dgs_software.sudoku.model;

import android.widget.Button;

public class Cell {
    // The Value of the cell (valid values are 1-9 and 0 if the cell is empty)
    private int value = 0;

    public int GetValue() {
        return this.value;
    }

    public void SetValue(int value) {
        if (value == this.value) {
            return;
        }

        if (value == 0) {
            this.value = value;
            this.isEmpty = true;
        } else if (value >= 1 && value <= 9) {
            this.value = value;
            this.isEmpty = false;
        } else {
            // Do nothing
        }
    }

    // Inicates if the cell is empty (equals a value of 0)
    private boolean isEmpty = true;

    public boolean GetIsEmpty() {
        return this.isEmpty;
    }

    public void SetIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
        if (this.isEmpty == true) {
            this.value = 0;
        }
    }

    // The button that corresponds to this cell
    private Button button;

    public Button GetButton() {
        return this.button;
    }

    public void SetButton(Button button) {
        this.button = button;
    }

    // Indicates if the value is fixed
    // Depending on the context this may have different meaning
    // SudokuSolver: Values entered by the user are fixed, values added by the solver algorithm are not fixed
    // SudokuPlay: The values on startup are fixed, the user entered values are non-fixed
    private boolean isFixedValue = false;

    public boolean GetIsFixedValue() {
        return this.isFixedValue;
    }

    public void SetIsFixedValue(boolean isFixed) {
        this.isFixedValue = isFixed;
    }

    public Cell(int value) {
        SetValue(value);
    }
}
