package dgs_software.sudoku.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Hashtable;
import java.util.Set;

import dgs_software.sudoku.R;
import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.model.Sudoku;
import dgs_software.sudoku.utils.Utils;

public class SudokuPlayActivity extends SudokuBaseActivity {

    protected void SetContentView() {
        setContentView(R.layout.activity_sudoku_play);
    }

    private boolean m_makeNotes = false;

    public boolean getMakeNotes() {
        return m_makeNotes;
    }

    public void setMakeNotes(boolean makeNotes) {
        m_makeNotes = makeNotes;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected Sudoku CreateSudokuModel() {
        // CREATE SUDOKU MODEL
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if (b != null)
            value = b.getInt("difficulty");
        Sudoku.Difficulty difficulty = null;
        if (value == 1) {
            difficulty = Sudoku.Difficulty.EASY;
        } else if (value == 2) {
            difficulty = Sudoku.Difficulty.NORMAL;
        } else {
            difficulty = Sudoku.Difficulty.HARD;
        }
        return new Sudoku(difficulty, getApplicationContext());
    }

    protected void InstantiateButtons() {
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
                ClearCellButtonClicked();
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

    @Override
    public Button[] CreateNoteButtons(int row, int col) {
        if (sudokuModel == null || sudokuModel.GetField() == null) {
            return null;
        }
        Cell[][] field = sudokuModel.GetField();

        Button[] noteButtons = new Button[9];
        for (int k = 0; k < noteButtons.length; k++) {
            noteButtons[k] = new Button(getApplicationContext());
        }
        field[row][col].SetNoteButtons(noteButtons);

        return noteButtons;
    }

    public void makeNoteButtonClickedAction() {
        setMakeNotes(!getMakeNotes());
        Button makeNoteButton = (Button) findViewById(R.id.makeNoteButton);
        if (getMakeNotes()) {
            makeNoteButton.setBackgroundColor(Color.GREEN);
        } else {
            makeNoteButton.setBackgroundColor(Color.GRAY);
        }
    }

    public void SudokuButtonClickedAction(int row, int col) {
        if (sudokuModel.GetField() == null || sudokuModel.GetField()[row][col] == null) {
            return;
        }
        if (activeCell != null && sudokuModel.GetField()[row][col].equals(activeCell)) {
            activeCell = null;
        } else {
            activeCell = sudokuModel.GetField()[row][col];
        }

        RefreshUI();
    }

    public void InputButtonClickedAction(int number) {
        if (activeCell == null) {
            return;
        }

        // Get row and column of active cell
        Pair<Integer, Integer> activeCellPosition = Utils.GetPositionOfCell(activeCell, sudokuModel);
        int row = activeCellPosition.first, col = activeCellPosition.second;

        if (getMakeNotes() == false) {
            sudokuModel.GetField()[row][col].SetValue(number);
            sudokuModel.GetField()[row][col].SetIsFixedValue(false);
        } else {
            if (sudokuModel.GetField()[row][col].GetIsEmpty() == false) {
                return; // It is not possible to overwrite a filled cell with a note
            }

            sudokuModel.GetField()[row][col].SetValue(0); // TODO: Schönere Lösung suchen
            boolean[] activeNotes = sudokuModel.GetField()[row][col].getActiveNotes();
            activeNotes[number-1] = !activeNotes[number-1];
            sudokuModel.GetField()[row][col].setActiveNotes(activeNotes);
        }
        RefreshUI();
    }

    public void DisplayErrorsButtonClicked() {
        //TODO
    }

    public void ClearCellButtonClicked() {
        if (activeCell != null && sudokuModel != null && sudokuModel.GetField() != null) {
            Pair<Integer, Integer> activeCellPosition = Utils.GetPositionOfCell(activeCell, sudokuModel);
            int row = activeCellPosition.first, col = activeCellPosition.second;
            if (sudokuModel.GetField()[row][col].GetIsFixedValue() == false) {
                activeCell.SetValue(0);
                activeCell.SetIsFixedValue(false);
            }
            sudokuModel.GetField()[row][col].ResetActiveNotes();

            RefreshUI();
        }
    }

    public void checkSolutionButtonClicked() {
        if (sudokuModel.SudokuIsValid() && sudokuModel.SudokuIsCompletelyFilled()) {
            // TODO: Dialog: Du hast gewonnen!
        } else {
            // TODO: Dialog: Du hast Verloren!
        }
    }

    private void DeleteNonFixedValues() {
        for (int i = 0; i < sudokuModel.GetField().length; i++) {
            for (int j = 0; j < sudokuModel.GetField()[i].length; j++) {
                if (sudokuModel.GetField()[i][j].GetIsFixedValue() == false) {
                    sudokuModel.GetField()[i][j].SetValue(0);
                }
            }
        }
    }
}