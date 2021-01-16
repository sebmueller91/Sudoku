package dgs_software.sudoku.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.security.InvalidParameterException;
import java.util.Hashtable;
import java.util.Set;

import dgs_software.sudoku.R;
import dgs_software.sudoku.config.Constants;
import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.model.Sudoku;
import dgs_software.sudoku.utils.Utils;

public class SudokuPlayActivity extends SudokuBaseActivity {

    // region Attributes
    // region makeNotes
    private boolean m_makeNotes = false;

    public boolean getMakeNotes() {
        return m_makeNotes;
    }

    public void setMakeNotes(boolean makeNotes) {
        m_makeNotes = makeNotes;
    }
    // endregion makeNotes

    // region NoteButtonsField
    private Button[][][] m_noteButtonsField;
    public Button[][][] getNoteButtons() {
        return this.m_noteButtonsField;
    }

    public void setNoteButtons(Button[][][] noteButtonsField) {
        this.m_noteButtonsField = noteButtonsField;
    }
    // endregion NoteButtonsField
    // endregion Attributes

    // region Methods
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_sudoku_play);
    }

    protected Sudoku createSudokuModel() {
        // CREATE SUDOKU MODEL
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if (b != null) {
            value = b.getInt(Sudoku.Difficulty.class.toString());
        } else {
            throw new InvalidParameterException(this.getClass().toString() + "Cannote create Sudoku Model: No bundle with key " + Sudoku.Difficulty.class.toString() + "found!");
        }
        Sudoku.Difficulty difficulty = Sudoku.Difficulty.intValToDifficulty(value);
        return new Sudoku(difficulty, getApplicationContext());
    }

    @Override
    protected void instantiateButtons() {
        // MAKE NOTES BUTTON
        Button makeNoteButton = (Button) findViewById(R.id.makeNoteButton);
        makeNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNoteButtonClickedAction();
            }
        });

        // CLEAR CELL BUTTON
        Button clearCellButton = (Button) findViewById(R.id.clearCellButton);
        clearCellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCellButtonClicked();
            }
        });

        // RESET SOLUTION BUTTON
        Button checkSolutionButton = (Button) findViewById(R.id.checkSolutionButton);
        checkSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSolutionButtonClicked();
            }
        });
    }

    // region CreateNestedGridLayout
    @Override
    protected GridLayout createNestedGridLayout(int row, int col, int buttonSize) {
        GridLayout nestedGridLayout = new GridLayout(getApplicationContext());
        nestedGridLayout.setRowCount(3);
        nestedGridLayout.setColumnCount(3);

        // Fill 3x3 Grid
        Button[] noteButtons = createNoteButtons(row, col);
        for (int k = 0; k < noteButtons.length; k++) {
            ViewGroup.LayoutParams noteButtonLayoutParams = new ViewGroup.LayoutParams(buttonSize, (int) buttonSize);
            noteButtons[k].setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.SUDOKU_NOTE_BUTTON_TEXT_SIZE);
            noteButtons[k].setPadding(0, 0, 0, 0);
            noteButtons[k].setTypeface(null, Typeface.BOLD);
            noteButtons[k].setStateListAnimator(null);
            final int rowFinal = row, colFinal = col;
            noteButtons[k].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sudokuButtonClickedAction(rowFinal, colFinal);
                }
            });
            nestedGridLayout.addView(noteButtons[k], k, noteButtonLayoutParams);
        }

        return nestedGridLayout;
    }

    // Creates a button array of length 9 to fill the 3x3 nestedGridLayout with the note buttons
    private  Button[] createNoteButtons(int row, int col) {
        if (getSudokuModel() == null || getSudokuModel().getField() == null) {
            return null;
        }

        // If not button Field not yet existent, create it
        if (getNoteButtons() == null) {
            setNoteButtons(new Button[9][9][9]);
        }

        // Fill the note button field for the given cell
        for (int k = 0; k < getNoteButtons().length; k++) {
            getNoteButtons()[row][col][k] = new Button(getApplicationContext());
        }

        // Return the note button array only for the given cell
        return getNoteButtons()[row][col];
    }
    // endregion CreateNestedGridLayout

    // region Button OnClickHandlers
    // region OnClickListener methods
    public void makeNoteButtonClickedAction() {
        setMakeNotes(!getMakeNotes());
        Button makeNoteButton = (Button) findViewById(R.id.makeNoteButton);
        if (getMakeNotes()) {
            makeNoteButton.setBackgroundColor(Color.GREEN);
        } else {
            makeNoteButton.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public void sudokuButtonClickedAction(int row, int col) {
        if (getSudokuModel().getField() == null || getSudokuModel().getField()[row][col] == null) {
            return;
        }
        if (getActiveCell() != null && getSudokuModel().getField()[row][col].equals(getActiveCell())) {
            setActiveCell(null);
        } else {
            setActiveCell(getSudokuModel().getField()[row][col]);
        }

        refreshUI();
    }

    @Override
    public void inputButtonClickedAction(int number) {
        if (getActiveCell() == null) {
            return;
        }

        // Get row and column of active cell
        Pair<Integer, Integer> activeCellPosition = Utils.getPositionOfCell(getActiveCell(), getSudokuModel());
        int row = activeCellPosition.first, col = activeCellPosition.second;

        if (getMakeNotes() == false) {
            getSudokuModel().getField()[row][col].setValue(number);
            getSudokuModel().getField()[row][col].setIsFixedValue(false);
            getSudokuButtonArray()[row][col].setVisibility(View.VISIBLE);
        } else {
            if (getSudokuModel().getField()[row][col].getIsEmpty() == false) {
                return; // It is not possible to overwrite a filled cell with a note
            }
            getSudokuButtonArray()[row][col].setVisibility(View.INVISIBLE); // Make top layer button invisible so the 3x3 grid behind it is visible
            getSudokuModel().getField()[row][col].setValue(0); // TODO: Schönere Lösung suchen
            boolean[] activeNotes = getSudokuModel().getField()[row][col].getActiveNotes();
            if (activeNotes == null) {
                activeNotes = new boolean[9];
            }
            activeNotes[number-1] = !activeNotes[number-1];
            getSudokuModel().getField()[row][col].setActiveNotes(activeNotes);
        }
        refreshUI();
    }

    public void clearCellButtonClicked() {
        if (getActiveCell() != null && getSudokuModel() != null && getSudokuModel().getField() != null) {
            Pair<Integer, Integer> activeCellPosition = Utils.getPositionOfCell(getActiveCell(), getSudokuModel());
            int row = activeCellPosition.first, col = activeCellPosition.second;
            if (getSudokuModel().getField()[row][col].getIsFixedValue() == false) {
                getActiveCell().setValue(0);
                getActiveCell().setIsFixedValue(false);
            }
            getSudokuModel().getField()[row][col].resetActiveNotes();

            refreshUI();
        }
    }

    public void checkSolutionButtonClicked() {
        if (getSudokuModel().isSolved()) {
            // TODO: Dialog: Du hast gewonnen!
        } else {
            // TODO: Dialog: Du hast Verloren!
        }
    }
    // endregion Button OnClickHandlers

    // endregion OnClickListener methods

    // region Refresh UI methods
    // Refresh the note Buttons of the User Interface
    @Override
    protected void refreshUI(boolean markFaultyCells) {
        super.refreshUI(markFaultyCells);
        SudokuCellStates[][] cellStates = getCellStates(markFaultyCells);

        for (int i = 0; i < getNoteButtons().length; i++) {
            for (int j = 0; j < getNoteButtons()[i].length; j++) {
                // Only if the cell is empty, the note fields must be drawn
                if (getNoteButtons() != null && getSudokuModel().getField()[i][j].getIsEmpty()) {
                    boolean[] activeNotes = getSudokuModel().getField()[i][j].getActiveNotes();
                    for (int k = 0; k < getNoteButtons()[i][j].length; k++) {
                        String noteButtonText = "";
                        if (activeNotes[k] == true) {
                            noteButtonText = new Integer(k + 1).toString();
                        }

                        int noteButtonRow = i * 3 + k / 3, noteButtonColumn = j * 3 + k % 3;
                        int backgroundColor = getResources().getColor(R.color.sudoku_button_inactive_background);
                        if (cellStates[i][j] == SudokuCellStates.HIGHLIGHTED_FIXED || cellStates[i][j] == SudokuCellStates.HIGHLIGHTED_NONFIXED) {
                            backgroundColor = getResources().getColor(R.color.sudoku_button_highlighted_background);
                        } else if (cellStates[i][j] == SudokuCellStates.ACTIVE_FIXED || cellStates[i][j] == SudokuCellStates.ACTIVE_NONFIXED) {
                            backgroundColor = getResources().getColor(R.color.sudoku_button_active_background);
                        }
                        SetNoteButtonProperties(noteButtonRow, noteButtonColumn, getNoteButtons()[i][j][k],noteButtonText, backgroundColor);
                    }
                }
            }
        }
    }

    // Updates all properties of one note-cell in the UI
    private void SetNoteButtonProperties(int row, int col, Button button, String buttonText, int backgroundColor) {
        if (button != null) {
            button.setTextColor(Color.BLACK); // TODO: Variabel machen
            button.setText(buttonText);

            GradientDrawable drawableInner = new GradientDrawable();
            GradientDrawable drawableBorders = new GradientDrawable();
            drawableInner.setColor(backgroundColor);
            drawableBorders.setColor(Constants.SUDOKU_BORDER_COLOR);

            Drawable[] layers = {drawableBorders, drawableInner};
            LayerDrawable layerDrawable = new LayerDrawable(layers);

            int[] borderThicknessValues = getNoteButtonBorderThicknessValues(row, col);
            layerDrawable.setLayerInset(1, borderThicknessValues[0], borderThicknessValues[1], borderThicknessValues[2], borderThicknessValues[3]);

            button.setBackground(layerDrawable);
        }
    }

    // Returns an array of the border thickness values for the given note-cell in the order [left,top,right,bottom]
    private static int[] getNoteButtonBorderThicknessValues(int row, int col) {
        int[] borders = new int[4];

        // Set Left Border Thickness
        if (col == 0) {
            borders[0] = Constants.STROKE_WIDTH_BIG_BORDER;
        } else if (col % 9 == 0) {
            borders[0] = Constants.STROKE_WIDTH_MID_BORDER;
        } else if (col % 3 == 0) {
            borders[0] = Constants.STROKE_WIDTH_SMALL_BORDER;
        } else {
            borders[0] = 0;
        }

        // Set Top Border Thickness
        if (row == 0) {
            borders[1] = Constants.STROKE_WIDTH_BIG_BORDER;
        } else if (row % 9 == 0) {
            borders[1] = Constants.STROKE_WIDTH_MID_BORDER;
        } else if (row % 3 == 0) {
            borders[1] = Constants.STROKE_WIDTH_SMALL_BORDER;
        } else {
            borders[1] = 0;
        }

        // Set Right Border Thickness
        if (col == 26) {
            borders[2] = Constants.STROKE_WIDTH_BIG_BORDER;
        } else if (col % 9 == 8) {
            borders[2] = Constants.STROKE_WIDTH_MID_BORDER;
        } else if (col % 3 == 2) {
            borders[2] = Constants.STROKE_WIDTH_SMALL_BORDER;
        } else {
            borders[2] = 0;
        }

        // Set Bottom Border Thickness
        if (row == 26) {
            borders[3] = Constants.STROKE_WIDTH_BIG_BORDER;
        } else if (row % 9 == 8) {
            borders[3] = Constants.STROKE_WIDTH_MID_BORDER;
        } else if (row % 3 == 2) {
            borders[3] = Constants.STROKE_WIDTH_SMALL_BORDER;
        } else {
            borders[3] = 0;
        }

        return borders;
    }
    // endregion Refresh UI methods
    // endregion Methods
}