package dgs_software.sudoku.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

public class SudokuPlayActivity extends SudokuBaseActivity {

    protected void SetContentView() {
        setContentView(R.layout.activity_sudoku_play);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected Sudoku CreateSudokuModel() {
        // CREATE SUDOKU MODEL
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null)
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
        // SOLVE SUDOKU BUTTON
        Button displayErrorsButton = (Button) findViewById(R.id.displayErrorsButton);
        displayErrorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayErrorsButtonClicked();
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

    public void SudokuButtonClickedAction(int row, int col) {
        /*int cellValue = GetButtonTextAsInteger(sudoku.GetField()[row][col].GetButton());
        String newText = Integer.toString(++cellValue);
        sudoku.GetField()[row][col].GetButton().setText(newText);
        sudoku.GetField()[row][col].GetButton().setText(newText);
        sudoku.GetField()[row][col].GetButton().setBackgroundColor(ACTIVE_COLOR_BACKGROUND);
        sudoku.GetField()[row][col].GetButton().setTextColor(ACTIVE_COLOR_FONT);
        sudoku.GetField()[row][col].GetButton().setTypeface(null, Typeface.BOLD);*/
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
        if (activeCell != null) {
            int row = 0, col = 0;
            for (int i = 0; i < sudokuModel.GetField().length; i++) {
                for (int j = 0; j < sudokuModel.GetField()[i].length; j++) {
                    if (sudokuModel.GetField()[i][j].equals(activeCell)) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            sudokuModel.GetField()[row][col].SetValue(number);
            sudokuModel.GetField()[row][col].SetIsFixedValue(false);
        }
        RefreshUI();
    }

    public void DisplayErrorsButtonClicked() {
        //TODO
    }

    public void ClearCellButtonClicked() {
        if (activeCell != null) {
            activeCell.SetValue(0);
            activeCell.SetIsFixedValue(false);
        }
        RefreshUI();
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