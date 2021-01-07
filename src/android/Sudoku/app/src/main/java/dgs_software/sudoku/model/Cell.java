package dgs_software.sudoku.model;

import android.view.View;
import android.widget.Button;

public class Cell {
    // region Attributes
    // region Value
    // The Value of the cell (valid values are 1-9 and 0 if the cell is empty)
    private int m_value = 0;

    public int GetValue() {
        return this.m_value;
    }

    public void SetValue(int value) {
        if (value == 0) {
            this.m_value = value;
            this.SetIsEmpty(true);
            if (this.GetButton() != null && GetNoteButtons() != null) {
                this.GetButton().setVisibility(View.INVISIBLE);
            }
        } else if (value >= 1 && value <= 9) {
            this.m_value = value;
            this.SetIsEmpty(false);
            if (this.GetButton() != null) {
                this.GetButton().setVisibility(View.VISIBLE);
            }
            ResetActiveNotes();
        } else {
            // Do nothing, should not happen
        }
    }

    // endregion Value

    // region isEmpty
    // Inicates if the cell is empty (equals a value of 0)
    private boolean m_isEmpty = true;

    public boolean GetIsEmpty() {
        return this.m_isEmpty;
    }

    public void SetIsEmpty(boolean isEmpty) {
        this.m_isEmpty = isEmpty;
        if (this.m_isEmpty == true) {
            if (GetValue() != 0) {
                this.m_value = 0;
            }
        }
    }
    // endregion

    // region IsFixedValue

    // Indicates if the value is fixed
    // Depending on the context this may have different meaning
    // SudokuSolver: Values entered by the user are fixed, values added by the solver algorithm are not fixed
    // SudokuPlay: The values on startup are fixed, the user entered values are non-fixed
    private boolean m_isFixedValue = false;

    public boolean GetIsFixedValue() {
        return this.m_isFixedValue;
    }

    public void SetIsFixedValue(boolean isFixed) {
        this.m_isFixedValue = isFixed;
    }
    // endregion

    // region Button
    // The button that corresponds to this cell
    private Button m_button;

    public Button GetButton() {
        return this.m_button;
    }

    public void SetButton(Button button) {
        this.m_button = button;
    }
    // endregion Button

    // region NoteButtons
    private Button[] m_noteButtons = null;
    public Button[] GetNoteButtons() {
        return m_noteButtons;
    }
    public void SetNoteButtons(Button[] noteButtons) {
        m_noteButtons = noteButtons;
    }
    // endregion

    // region activeNotes
    private boolean[] m_activeNotes = new boolean[9];

    public boolean[] getActiveNotes() {
        return this.m_activeNotes;
    }

    public void setActiveNotes(boolean[] activeNotes) {
        this.m_activeNotes = activeNotes;
    }

    public void ResetActiveNotes() {
        boolean[] activeNotes = getActiveNotes();
        if (activeNotes != null) {
            for (int i = 0; i < activeNotes.length; i++) {
                activeNotes[i] = false;
            }
        }
    }

    // endregion activeNotes
    // endregion attributes

    // region Constructor
    public Cell(int value) {
        SetValue(value);
    }
    // endregion Constructor
}