package dgs_software.sudoku;

import android.widget.Button;

public class Cell {
    private int value = 0;
    private boolean isEmpty = true;
    private Button button;

    public int GetValue() {
        return this.value;
    }

    public void SetValue(int value) {
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

    public boolean GetIsEmpty() {
        return this.isEmpty;
    }

    public void SetIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
        if (this.isEmpty == true) {
            this.value = 0;
        }
    }

    public Button GetButton() {
        return this.button;
    }

    public void SetButton(Button button) {
        this.button = button;
    }
}
