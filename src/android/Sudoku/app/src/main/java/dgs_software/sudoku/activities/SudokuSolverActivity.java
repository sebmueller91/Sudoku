package dgs_software.sudoku.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.Hashtable;
import java.util.Set;

import dgs_software.sudoku.R;
import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.model.Sudoku;
import dgs_software.sudoku.utils.Utils;

public class SudokuSolverActivity extends SudokuBaseActivity {
    protected void SetContentView() {
        setContentView(R.layout.activity_sudoku_solver);
    }

    protected Sudoku CreateSudokuModel() {
        Cell[][] field = new Cell[9][9];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new Cell(0);
            }
        }
        return new Sudoku(field);
    }
    protected void InstantiateButtons() {
        // SOLVE SUDOKU BUTTON
        Button solveSudokuButton = (Button) findViewById(R.id.solveSudokuButton);
        solveSudokuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolveSudokuButonClicked();
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
        Button resetSolutionButton = (Button) findViewById(R.id.resetSolutionButton);
        resetSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetSolutionButtonClicked();
            }
        });
    }

    @Override
    public Button[] CreateNoteButtons(int row, int col) {
        return null; // No note buttons needed
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
        DeleteNonFixedValues();
        if (activeCell != null) {
            Pair<Integer, Integer> activeCellPosition = Utils.GetPositionOfCell(activeCell, sudokuModel);
            int row = activeCellPosition.first, col = activeCellPosition.second;

            sudokuModel.GetField()[row][col].SetValue(number);
            sudokuModel.GetField()[row][col].SetIsFixedValue(true);
        }
        RefreshUI();
    }

    public void SolveSudokuButonClicked() {
        sudokuModel.GetSolution();
        RefreshUI();
    }

    public void ClearCellButtonClicked() {
        if (activeCell != null) {
            activeCell.SetValue(0);
            activeCell.SetIsFixedValue(false);
        }
        RefreshUI();
    }

    public void ResetSolutionButtonClicked() {
        DeleteNonFixedValues();
        RefreshUI();
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